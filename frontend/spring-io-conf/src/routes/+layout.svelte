<div class="container">
    <nav class="navbar">
        <div class="container navbar__inner">
            <a class="navbar__logo" href="/">
                <img alt="Spring IO Logo" src="/logo-spring-io.svg">
            </a>

            <!-- links come next, with auto margin to push future items to the far right -->
            <ul class="navbar__links">
                <li><a href="/">Sessions</a></li>
                <li><a href="/about">About</a></li>
            </ul>

            <!-- future component placeholder -->
            <div class="navbar__actions">
                <!-- e.g. a profile icon, search bar, etc. -->
            </div>
        </div>
    </nav>

    <slot/>
</div>