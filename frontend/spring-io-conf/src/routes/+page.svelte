<script>
    import {onMount} from 'svelte';

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
            <table class="sessions-table">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Speakers</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {#each sessions as session}
                    <tr>
                        <td>{session.title}</td>
                        <td>{session.speakers}</td>
                        <td>{session.price}</td>
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