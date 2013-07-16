<?php
require_once '_init.php';
require_once '_check.php';

$hits = array();
$q = "";
if (array_key_exists("q", $_GET)) {
    $q = $_GET["q"];
    $stmt = $conn->prepare("SELECT * FROM player WHERE player_name LIKE ? ORDER BY player_name LIMIT 20");
    $stmt->execute(array($q));

    $hits = $stmt->fetchAll();
}
?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Tregmine Admin Tool</title>
    <style type="text/css">
    @import 'style.css';
    </style>
</head>
<body>
    <div id="layout_wrapper">
        <h1>Tregmine Admin Tool</h1>

        <?php require 'menu.php'; ?>

        <h2>User search</h2>

        <form method="get" action="search.php">
            <div class="fieldwrapper">
                <label for="q">User</label>
                <div class="field">
                    <input type="text" name="q" id="q" value="<?php echo htmlspecialchars($q); ?>"/>
                </div>
            </div>

            <div class="buttonwrapper">
                <button type="submit">Search</button>
            </div>
        </form>

        <?php if ($q): ?>
            <h2>Results for "<?php echo htmlspecialchars($q); ?>"</h2>

            <?php if ($hits): ?>
                <table style="width: 100%;">
                    <cols>
                        <col style="width: 50px;" />
                        <col />
                        <col style="width: 200px;" />
                        <col style="width: 150px;" />
                        <col style="width: 200px;" />
                    </cols>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Member Since</th>
                        <th>Wallet</th>
                        <th>Actions</th>
                    </tr>
                    <?php foreach ($hits as $hit): ?>
                    <tr>
                        <td>
                            <?php echo $hit["player_id"]; ?>
                        </td>
                        <td>
                            <?php echo $hit["player_name"]; ?>
                        </td>
                        <td>
                            <?php echo $hit["player_created"]; ?>
                        </td>
                        <td>
                            <?php echo $hit["player_wallet"]; ?> tregs
                        </td>
                        <td>
                            &raquo; <a href="player_report.php?id=<?php echo $hit["player_id"]; ?>">Reports</a><br />
                            &raquo; <a href="player_stats.php?id=<?php echo $hit["player_id"]; ?>">Stats</a><br />
                            <?php if (array_key_exists("admin", $_SESSION)): ?>
                            &raquo; <a href="player_perm.php?id=<?php echo $hit["player_id"]; ?>">Permissions</a><br />
                            <?php endif; ?>
                        </td>
                    </tr>
                    <?php endforeach; ?>
                </table>
            <?php else: ?>
                <p>No Hits.</p>
            <?php endif; ?>
        <?php endif; ?>
    </div>
</body>
</html>
