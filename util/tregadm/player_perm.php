<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

if (!array_key_exists("senioradmin", $_SESSION)) {
    header('Location: index.php');
    exit;
}

$player = array();
$settings = array();
if (array_key_exists("id", $_GET)) {
    $stmt = $conn->prepare("SELECT * FROM player WHERE player_id = ?");
    $stmt->execute(array($_GET["id"]));

    $player = $stmt->fetch();

    $stmt->closeCursor();

    $stmt = $conn->prepare("SELECT * FROM player_property WHERE player_id = ?");
    $stmt->execute(array($_GET["id"]));

    $rawSettings = $stmt->fetchAll();
    foreach ($rawSettings as $setting) {
        $settings[$setting["property_key"]] = $setting["property_value"];
    }
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
        <h1 id="banner"><span>Tregmine Admin Tool</span></h1>

        <?php require 'menu.php'; ?>

        <h2 class="info"><?php echo $player["player_name"]; ?> (<?php echo $player["player_id"]; ?>)</h2>

        <div class="col75">

            <table class="info">
                <tr>
                    <th colspan="9" class="infoHeader">Properties</th>
                </tr>

                <?php foreach ($settings as $key => $value):
                    if (array_key_exists($key, $permissionList) && $key != "color") continue;
                    ?>
                    <tr>
                        <td><?php echo $key; ?></td>
                        <td><?php echo $value; ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>

            <form method="post" action="player_perm_save.php?id=<?php echo $player["player_id"]; ?>">

            <table class="info">
                <tr>
                    <th colspan="9" class="infoHeader">Permissions</th>
                </tr>

                <?php foreach ($permissionList as $key => $name): ?>
                    <tr>
                        <td><input type="checkbox" name="perm[<?php echo $key; ?>]" id="perm_<?php echo $key; ?>" value="1" <?php if (array_key_exists($key, $settings) && $settings[$key] == "true") echo ' checked="checked"'; ?> /></td>
                        <td><label for="perm_<?php echo $key; ?>"><?php echo $name; ?></label></td>
                    </tr>
                <?php endforeach; ?>
            </table>

            <h3 class="infoHeader">Other</h3>

            <div class="field">
                <label for="password">Password</label>
                <div class="element">
                    <select name="color" id="color">
                        <?php foreach ($colors as $name => $color): ?>
                            <option value="<?php echo $name; ?>" <?php if (array_key_exists("color", $settings) && $settings["color"] == $name) echo 'selected="selected"'; ?>><?php echo $name; ?> (<?php echo $color; ?>)</option>
                        <?php endforeach; ?>
                    </select>
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="field">
                <label for="password">Password</label>
                <div class="element">
                    <input type="text" name="password" id="password" />
                    <p>
                        The password will only be changed if you enter something in this box.
                    </p>
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="button">
                <button type="submit">Save changes</button>
            </div>

            </form>

        </div>

        <div class="col25">

            <h3 class="actionsHeader">Actions</h3>

            <ul class="actions">
                <li><a href="player_report.php?id=<?php echo $player["player_id"]; ?>">Reports</a></li>
                <li><a href="player_stats.php?id=<?php echo $player["player_id"]; ?>">Stats</a></li>
            </ul>

        </div>

        <div class="col_clear">&nbsp;</div>
    </div>
</body>
</html>
