<ul id="menu">
    <li><a href="start.php">Start</a></li>
    <?php if (array_key_exists("admin", $_SESSION)): ?>
    <li><a href="chatlog.php">Chat Logs</a></li>
    <li><a href="zones.php">Zones</a></li>
    <?php endif; ?>
    <li><a href="reports.php">Reports</a></li>
    <li><a hreF="logout.php">Logout</a></li>
</ul>

