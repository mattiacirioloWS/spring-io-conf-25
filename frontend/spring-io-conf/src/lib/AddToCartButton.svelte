<script>
    import {createEventDispatcher} from 'svelte';

    export let attendeeId;
    export let sessionId;

    const dispatch = createEventDispatcher();
    let loading = false;

    async function handleClick() {
        if (!attendeeId) {
            alert('Please select an attendee first.');
            return;
        }

        loading = true;
        try {
            const res = await fetch('/api/orders', {
                method: 'PUT',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({
                    attendeeId,
                    itemId: sessionId
                })
            });

            const data = await res.json();
            if (!res.ok) {
                throw new Error(data.detail || 'Failed to add session to the cart');
            }

            dispatch('orderCreated', {orderId: data.orderId});

        } catch (err) {
            alert(err.message);
        } finally {
            loading = false;
        }
    }
</script>

<button class="add-to-cart-button" disabled={loading} on:click={handleClick}>
    {#if loading}
        Adding…
    {:else}
        🛒 Add
    {/if}
</button>

<style>
    .add-to-cart-button {
        background: var(--clr-accent);
        color: var(--clr-light);
        border: none;
        border-radius: 0.375rem;
        padding: 0.5rem 0.75rem;
        cursor: pointer;
        font-size: 0.875rem;
        transition: background 0.2s;
    }

    .add-to-cart-button:hover:enabled {
        background: var(--clr-light);
        color: var(--clr-accent);
    }

    .add-to-cart-button:disabled {
        opacity: 0.6;
        cursor: not-allowed;
    }
</style>