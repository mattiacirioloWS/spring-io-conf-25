<script>
    export let attendeeId;

    let order = null;
    let loading = false;

    // Allow parent to manually refresh if needed
    export async function refresh() {
        await fetchOrder();
    }

    // Whenever attendeeId changes, re-fetch
    $: if (attendeeId) {
        fetchOrder();
    }

    async function fetchOrder() {
        loading = true;
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
        } catch (err) {
            console.error(err);
            order = null;
        } finally {
            loading = false;
        }
    }
</script>

{#if loading}
    <p class="order-summary__loading">Loading order…</p>
{:else if order}
    <div class="order-summary">
        <h3 class="order-summary__title">🧾 Order Summary</h3>
        <ul class="order-summary__list">
            {#each order.items as item}
                <li>{item.name} — €{item.price.toFixed(2)}</li>
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

    .order-summary__loading {
        font-style: italic;
        margin-bottom: 1rem;
    }
</style>