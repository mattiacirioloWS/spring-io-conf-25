<script>
    import {onMount} from 'svelte';

    export let attendeeId;
    let attendees = [];
    let loading = false;

    onMount(async () => {
        loading = true;
        const res = await fetch('/api/attendees');
        attendees = await res.json();
        loading = false;
    });

    function handleSelect(event) {
        attendeeId = event.target.value;
    }
</script>

<div class="attendee-select">
    <label class="attendee-select__label" for="attendee">Select attendee:</label>
    {#if loading}
        <span class="attendee-select__loading">Loading attendees…</span>
    {:else}
        <select id="attendee" on:change={handleSelect} bind:value={attendeeId}
                class="attendee-select__dropdown">
            <option value="" disabled selected>Select an attendee</option>
            {#each attendees as attendee}
                <option value={attendee.id}>{attendee.firstName} {attendee.lastName}</option>
            {/each}
        </select>
    {/if}
</div>

<style>
    .attendee-select {
        margin: 1rem 0;
    }

    .attendee-select__label {
        margin-right: 0.5rem;
        font-weight: 500;
    }

    .attendee-select__dropdown {
        padding: 0.5rem;
        border-radius: 0.375rem;
        border: 1px solid var(--clr-border);
        min-width: 200px;
    }

    .attendee-select__loading {
        margin-left: 0.5rem;
        font-style: italic;
    }
</style>
