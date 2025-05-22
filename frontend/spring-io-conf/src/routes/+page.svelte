<script>
    import SessionsTable from '$lib/SessionsTable.svelte';
    import OrderSummary from '$lib/OrderSummary.svelte';
    import AttendeeSelector from '$lib/AttendeeSelector.svelte';

    let selectedAttendeeId = null;
    let orderSummaryRef;
</script>

<div>
    <header class="header">
        <h2>Sessions 23 May 2025</h2>
    </header>

    <main class="main">
        <AttendeeSelector bind:attendeeId={selectedAttendeeId}/>

        {#if selectedAttendeeId}
            <OrderSummary bind:this={orderSummaryRef} attendeeId={selectedAttendeeId}/>
        {/if}

        <SessionsTable
                attendeeId={selectedAttendeeId}
                onItemAdded={() => orderSummaryRef?.refresh()}
        />
    </main>

    <footer class="footer">
        <p>&copy; 2025 Spring IO Conf</p>
    </footer>
</div>