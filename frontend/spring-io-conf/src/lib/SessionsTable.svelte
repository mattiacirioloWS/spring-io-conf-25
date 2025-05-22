<script>
    import {onMount} from 'svelte';
    import EditablePrice from '$lib/EditablePrice.svelte';
    import AddToCartButton from '$lib/AddToCartButton.svelte';

    export let attendeeId = null;
    export let onItemAdded = () => {
    };

    let sessions = [];
    let search = '';
    let loading = false;

    onMount(fetchSessions);

    async function fetchSessions(q = '') {
        loading = true;
        const url = q
            ? `/api/sessions/search/${encodeURIComponent(q)}`
            : `/api/sessions`;
        const res = await fetch(url);
        sessions = await res.json();
        loading = false;
    }

    function handleSearch() {
        fetchSessions(search);
        window.scrollTo({top: 0});
    }

    function handlePriceUpdate({sessionId, price}) {
        const idx = sessions.findIndex(s => s.id === sessionId);
        if (idx > -1) sessions[idx].price = price;
    }
</script>

<form class="search-form" on:submit|preventDefault={handleSearch}>
    <input bind:value={search} class="search-form__title" placeholder="Search by title…"/>
    <button class="search-form__submit">Search</button>
</form>

{#if loading}
    <p>Loading…</p>
{:else if !sessions.length}
    <p>No sessions found.</p>
{:else}
    <table class="w-full table-auto border-collapse shadow bg-white">
        <thead class="bg-gray-800 text-white">
        <tr>
            <th class="px-4 py-2">Title</th>
            <th class="px-4 py-2">Speakers</th>
            <th class="px-4 py-2">Price</th>
            {#if attendeeId}
                <th class="px-4 py-2">Add to cart</th>
            {/if}
        </tr>
        </thead>
        <tbody>
        {#each sessions as session (session.id)}
            <tr class="hover:bg-gray-50">
                <td class="border px-4 py-2">{session.title}</td>
                <td class="border px-4 py-2">{session.speakers}</td>
                <td class="border px-4 py-2">
                    <EditablePrice
                            sessionId={session.id}
                            price={session.price}
                            onUpdate={handlePriceUpdate}
                    />
                </td>
                {#if attendeeId}
                    <td class="border px-4 py-2 text-center">
                        <AddToCartButton
                                attendeeId={attendeeId}
                                sessionId={session.id}
                                on:orderCreated={onItemAdded}
                        />
                    </td>
                {/if}
            </tr>
        {/each}
        </tbody>
    </table>
{/if}

<style>
    .search-form {
        display: flex;
        align-items: center;
    }

    .search-form__title,
    .search-form__submit {
        display: flex;
        align-items: center;
        box-sizing: border-box;
        margin-top: 0.5rem;
        margin-bottom: 0.5rem;
    }

    .search-form__title {
        flex: 1;
        padding: 0 0.75rem;
        border: 1px solid var(--clr-border);
        border-radius: 0.375rem 0 0 0.375rem;
        min-height: 2.5rem;
    }

    .search-form__submit {
        flex: 0 0 auto;
        padding: 0 1rem;
        border: 1px solid var(--clr-accent);
        background: var(--clr-accent);
        color: var(--clr-light);
        border-radius: 0 0.375rem 0.375rem 0;
        min-height: 2.5rem;
        cursor: pointer;
        transition: background 0.2s, color 0.2s;
    }

    .search-form__submit:hover {
        background: var(--clr-light);
        color: var(--clr-accent);
    }

    table {
        width: 100%;
        border-collapse: collapse;
        background: var(--clr-light);
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
    }

    thead {
        background: var(--clr-accent);
        color: var(--clr-light);
    }

    th,
    td {
        padding: 0.75rem 1rem;
        border: 1px solid var(--clr-border);
        text-align: left;
        font-size: var(--fs-sm);
    }

    tbody tr:nth-child(even) {
        background: #f9fafb;
    }

    tbody tr:hover {
        background: #eef2ff;
    }

    th {
        font-weight: 600;
    }
</style>