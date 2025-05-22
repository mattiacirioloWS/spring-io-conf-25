<script>
    import {onDestroy} from 'svelte';

    export let attendeeId;

    let order = null;
    let loading = false;

    let prevOrderId;
    let changedItems = new Set();
    let totalChanged = false;

    let errorMsg = '';

    let successMessage = '';
    let successType = '';
    let buttonsEnabled = true;

    let eventSource;

    $: if (order?.id && order.id !== prevOrderId) {
        changedItems = new Set();
        totalChanged = false;
        errorMsg = '';
        buttonsEnabled = true;
        prevOrderId = order.id;
    }

    function clearOrder() {
        order = null;
        changedItems = new Set();
        totalChanged = false;
        errorMsg = '';
        successMessage = '';
        successType = '';
        buttonsEnabled = true;
        closeSse();
    }

    // Parent may manually trigger reload:
    export async function refresh() {
        await fetchOrder();
    }

    // Whenever attendeeId changes, re-fetch & re-subscribe
    $: if (attendeeId) {
        fetchOrder();
    } else {
        // no attendee: tear down
        clearOrder();
    }

    onDestroy(clearOrder);

    async function fetchOrder() {
        loading = true;
        clearOrder();
        try {
            const res = await fetch(`/api/orders/byAttendee/${attendeeId}`);
            if (!res.ok) {
                clearOrder();
                return;
            }
            if (res.status === 204) {
                order = null;
                return;
            }

            order = await res.json();

            eventSource = new EventSource(`/api/orders/${order.id}/changes`);
            eventSource.addEventListener('orderItemPriceChanged', e => {
                const {orderItemId, price: {amount: priceAmount}} = JSON.parse(e.data);

                order.items = order.items.map(item =>
                    item.id === orderItemId ? {...item, price: priceAmount} : item
                );
                changedItems.add(orderItemId);
            });

            eventSource.addEventListener('orderTotalChanged', e => {
                const {total: {amount: totalAmount}} = JSON.parse(e.data);
                order.total = totalAmount;
                totalChanged = true;
            });

        } catch (err) {
            console.error(err);
            clearOrder();
        } finally {
            loading = false;
        }
    }

    function closeSse() {
        if (eventSource) {
            eventSource.close();
            eventSource = null;
        }
    }

    function dismissAlert(itemId) {
        changedItems.delete(itemId);
        changedItems = new Set(changedItems);
    }

    function markSubmitted() {
        markClosed('Order submitted', 'success');
    }

    function markCanceled() {
        markClosed('Order canceled', 'warning');
    }

    function markClosed(message, type) {
        successMessage = message;
        successType = type;
        buttonsEnabled = false;
        setTimeout(clearOrder, 3000);
    }

    async function submitOrder() {
        try {
            const res = await fetch(`/api/orders/${order.id}/submit`, {method: 'PUT'});
            if (!res.ok) throw new Error('Submit failed');
            markSubmitted();
        } catch (err) {
            errorMsg = err.message;
        }
    }

    async function cancelOrder() {
        try {
            const res = await fetch(`/api/orders/${order.id}/cancel`, {method: 'DELETE'});
            if (!res.ok) throw new Error('Cancel failed');
            markCanceled();
        } catch (err) {
            errorMsg = err.message;
        }
    }
</script>

{#if loading}
    <p class="order-summary__loading">Loading order…</p>
{:else if order}
    <div class="order-summary">
        <h3 class="order-summary__title">🧾 Order Summary</h3>
        <ul class="order-summary__list">
            {#each order.items as item (item.id)}
                <li>
                    <div class="order-item">
                        <span>{item.name} — €{item.price.toFixed(2)}</span>

                        {#if changedItems.has(item.id)}
                            <span class="price-alert">
                                Price updated
                                <button class="close-btn" on:click={() => dismissAlert(item.id)}>×</button>
                            </span>
                        {/if}
                    </div>
                </li>
            {/each}
        </ul>

        <div class="order-summary__total-row">
            <div class="order-summary__total-value">
                <strong>Total:</strong> €{order.total.toFixed(2)}
                {#if totalChanged}
                        <span class="price-alert">
                            Total updated
                            <button class="close-btn" on:click={() => totalChanged = false}>×</button>
                        </span>
                {/if}
            </div>
            <div class="order-actions">
                <button class="submit-btn" class:disabled={!buttonsEnabled} disabled={!buttonsEnabled}
                        on:click={submitOrder}>Submit
                </button>
                <button class="cancel-btn" class:disabled={!buttonsEnabled} disabled={!buttonsEnabled}
                        on:click={cancelOrder}>Cancel
                </button>
            </div>
        </div>
        {#if successMessage}
            <div class="alert alert-{successType}">
                {successMessage}
                <button class="close-btn" on:click={clearOrder}>×</button>
            </div>
        {/if}
        {#if errorMsg}
            <p class="order-error">⚠️ {errorMsg}</p>
        {/if}
    </div>
{/if}

<style>
    .order-summary {
        background: #f8fafc;
        border: 1px solid var(--clr-border);
        border-radius: 0.5rem;
        padding: 1rem;
        margin-bottom: 1.5rem;
    }

    .order-summary__title {
        font-size: 1.125rem;
        font-weight: 600;
        margin-bottom: 0.75rem;
    }

    .order-summary__list {
        list-style: none;
        padding-left: 0;
        margin-bottom: 0.5rem;
    }

    .order-item {
        position: relative;
        padding-bottom: 0.5rem;
        display: inline-flex;
        justify-content: space-between;
        align-items: center;

    }

    .price-alert {
        background-color: #fff3cd;
        color: #856404;
        border: 1px solid #ffeeba;
        border-radius: 0.25rem;
        margin-left: 0.5rem;
        font-size: 0.875rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .close-btn {
        background: none;
        border: none;
        font-weight: bold;
        cursor: pointer;
        color: #856404;
        margin-left: 1rem;
    }

    .order-summary__loading {
        font-style: italic;
        margin-bottom: 1rem;
    }

    .order-summary__total-row {
        display: flex;
        width: 100%;
        justify-content: space-between;
        align-items: center;
    }

    .order-summary__total-value {
        display: inline-flex;
        justify-content: space-between;
        align-items: center;
    }

    .order-actions {
        display: flex;
        gap: 0.5rem;
    }

    .order-actions button {
        padding: 0.25rem 0.75rem;
        border-radius: 0.25rem;
        border: 1px solid var(--clr-border);
        cursor: pointer;
    }

    .cancel-btn {
        background: #721c24;
    }

    .cancel-btn:hover {
        background: #f5c6cb;
        color: #721c24;
    }

    .order-error {
        color: #842029;
        background: #f8d7da;
        border: 1px solid #f5c2c7;
        padding: 0.5rem;
        border-radius: 0.25rem;
        margin-top: 0.5rem;
    }

    button.disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }

    .alert {
        padding: 0.75rem 1rem;
        border-radius: 0.375rem;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 1rem;
    }

    .alert-success {
        background: #d1e7dd;
        color: #0f5132;
        border: 1px solid #badbcc;
    }

    .alert-warning {
        background: #fff3cd;
        color: #664d03;
        border: 1px solid #ffecb5;
    }

    .alert .close-btn {
        background: none;
        border: none;
        font-size: 1rem;
        cursor: pointer;
    }
</style>
