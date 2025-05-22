<script>
    import {onMount} from 'svelte';
    import EditablePrice from '$lib/EditablePrice.svelte';


    let sessions = [];
    let search = '';
    let loading = false;

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

    onMount(() => fetchSessions());
</script>

<div>
    <header class="header">
        <h2>Sessions 23 May 2025</h2>
    </header>
    <main class="main">
        <form class="search-form" on:submit|preventDefault={handleSearch}>
            <input bind:value={search} class="search-form__title" placeholder="Search by title…"/>
            <button class="search-form__submit">Search</button>
        </form>

        {#if loading}<p>Loading…</p>
        {:else if !sessions.length}<p>No sessions found.</p>
        {:else}
            <table class="w-full table-auto border-collapse shadow bg-white">
                <thead class="bg-gray-800 text-white">
                <tr>
                    <th class="px-4 py-2">Title</th>
                    <th class="px-4 py-2">Speakers</th>
                    <th class="px-4 py-2">Price</th>
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
                    </tr>
                {/each}
                </tbody>
            </table>
        {/if}
    </main>
    <footer class="footer">
        <p>&copy; 2025 Spring IO Conf</p>
    </footer>
</div>

<style>
    @import './style.css';
</style>