<?php
require_once '_init.php';
require_once '_check.php';

if (!array_key_exists("admin", $_SESSION)) {
    header('Location: index.php');
    exit;
}

$q = array_key_exists("q", $_GET) ? $_GET["q"] : "";

$params = array();
$sql  = "SELECT * FROM zone ";
if ($q) {
    $sql  .= "WHERE zone_name LIKE ? ";
    $params[] = $q;
}
$sql .= "ORDER BY zone_name";

$stmt = $conn->prepare($sql);
$stmt->execute($params);

$hits = $stmt->fetchAll();
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
        <h1 id="banner"><span>Tregmine Admin Tool</span></h1>

        <?php require 'menu.php'; ?>

        <h2 class="info">Zones</h2>

        <form method="get" action="zones.php">
            <div class="field">
                <label for="q">Zone</label>
                <div class="element">
                    <input type="text" name="q" id="q" value="<?php echo htmlspecialchars($q); ?>"/>
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="button">
                <button type="submit">Search</button>
            </div>
        </form>

        <h3 class="infoHeader"><?php echo count($hits); ?> results for "<?php echo htmlspecialchars($q); ?>"</h3>

        <?php if ($hits): ?>
            <table style="width: 100%;" class="info">
                <cols>
                    <col style="width: 50px;" />
                    <col />
                    <col style="width: 200px;" />
                    <col style="width: 200px;" />
                </cols>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>World</th>
                    <th>Actions</th>
                </tr>
                <?php foreach ($hits as $hit): ?>
                <tr>
                    <td>
                        <?php echo $hit["zone_id"]; ?>
                    </td>
                    <td>
                        <?php echo $hit["zone_name"]; ?>
                    </td>
                    <td>
                        <?php echo $hit["zone_world"]; ?>
                    </td>
                    <td>
                        &raquo; <a href="zone_info.php?id=<?php echo $hit["zone_id"]; ?>">Info</a>
                        <?php if (array_key_exists("senioradmin", $_SESSION)): ?>
                        <?php endif; ?>
                    </td>
                </tr>
                <?php endforeach; ?>
            </table>
        <?php else: ?>
            <p>No Hits.</p>
        <?php endif; ?>
    </div>
</body>
</html>
