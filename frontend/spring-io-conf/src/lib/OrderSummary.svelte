<script>
    import {onDestroy} from 'svelte';

    export let attendeeId;

    let order = null;
    let loading = false;

    let prevOrderId;
    let changedItems = new Set();

    let eventSource;

    $: if (order?.id && order.id !== prevOrderId) {
        changedItems = new Set();
        prevOrderId = order.id;
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
        order = null;
        closeSse();
    }

    onDestroy(closeSse);

    async function fetchOrder() {
        loading = true;
        closeSse();
        try {
            const res = await fetch(`/api/orders/byAttendee/${attendeeId}`);
            if (!res.ok) {
                order = null;
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
        } catch (err) {
            console.error(err);
            order = null;
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
</style>