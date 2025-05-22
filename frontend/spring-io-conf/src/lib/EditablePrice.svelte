<script>
    import {tick} from 'svelte';

    /**
     * @param {string} sessionId
     * @param {number} price
     * @param {(detail: { sessionId: string; price: number }) => void} onUpdate
     */
    export let sessionId;
    export let price;

    export let onUpdate = () => {
    };

    let editing = false;
    let inputValue = price.toFixed(2);
    let saving = false;
    let error = null;
    let oldPrice;

    function startEdit() {
        editing = true;
        oldPrice = price;
        tick().then(() => {
            const el = document.getElementById(`price-${sessionId}`);
            if (el) el.focus();
        });
    }

    async function save() {
        const newPrice = parseFloat(inputValue);
        if (isNaN(newPrice) || newPrice < 0) {
            error = 'Invalid price';
            return;
        }

        saving = true;
        error = null;
        onUpdate({sessionId, price: newPrice});  // optimistic

        try {
            const res = await fetch(`/api/sessions/${sessionId}/price/${newPrice}`, {
                method: 'POST'
            });
            if (!res.ok) {
                onUpdate({sessionId, price: oldPrice}); // rollback
                throw new Error(await res.text());
            }
            price = newPrice;
        } catch (e) {
            onUpdate({sessionId, price});            // rollback
            error = 'Failed to save';
            console.error(e);
        } finally {
            saving = false;
            editing = false;
        }
    }

    function handleKeydown(e) {
        if (e.key === 'Enter') save();
        if (e.key === 'Escape') {
            editing = false;
            inputValue = price.toFixed(2);
            error = null;
        }
    }
</script>

<div class="relative inline-flex items-center">
    {#if editing}
        <input
                id={"price-" + sessionId}
                class="w-20 p-1 border rounded text-right text-sm"
                bind:value={inputValue}
                on:keydown={handleKeydown}
                disabled={saving}
        />
        <button
                class="ml-1 px-2 py-1 bg-green-500 text-white rounded disabled:opacity-50"
                on:click={save}
                disabled={saving}
                title="Save"
        >💾
        </button>
    {:else}
        <div class="inline-flex items-center cursor-pointer hover:bg-gray-100 px-1 rounded"
             on:click={startEdit}
             role="button"
             aria-label="Edit price"
             title="Edit price">
            <span class="whitespace-nowrap">€{price.toFixed(2)} ✏️</span>
        </div>

    {/if}

    {#if error}
        <div class="absolute top-full left-0 mt-1 text-red-600 text-xs">
            {error}
        </div>
    {/if}
</div>