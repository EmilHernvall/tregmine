<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

if (!array_key_exists("admin", $_SESSION)) {
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
        <h1>Tregmine Admin Tool</h1>

        <?php require 'menu.php'; ?>

        <h2><?php echo $player["player_name"]; ?> (<?php echo $player["player_id"]; ?>)</h2>

        <div class="col50">

            <h3>Properties</h3>

            <table>
                <?php foreach ($settings as $key => $value):
                    if (array_key_exists($key, $permissionList) && $key != "color") continue;
                    ?>
                    <tr>
                        <td><?php echo $key; ?></td>
                        <td><?php echo $value; ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>

            <h3>Actions</h3>

            <ul>
                <li><a href="player_report.php?id=<?php echo $player["player_id"]; ?>">Reports</a></li>
                <li><a href="player_stats.php?id=<?php echo $player["player_id"]; ?>">Stats</a></li>
            </ul>

        </div>

        <div class="col50">

            <form method="post" action="player_perm_save.php?id=<?php echo $player["player_id"]; ?>">

            <h3>Color</h3>

            <select name="color" id="color">
                <?php foreach ($colors as $name => $color): ?>
                    <option value="<?php echo $name; ?>" <?php if (array_key_exists("color", $settings) && $settings["color"] == $name) echo 'selected="selected"'; ?>><?php echo $name; ?> (<?php echo $color; ?>)</option>
                <?php endforeach; ?>
            </select>

            <h3>Permissions</h3>

            <table>
                <?php foreach ($permissionList as $key => $name): ?>
                    <tr>
                        <td><input type="checkbox" name="perm[<?php echo $key; ?>]" id="perm_<?php echo $key; ?>" value="1" <?php if (array_key_exists($key, $settings) && $settings[$key] == "true") echo ' checked="checked"'; ?> /></td>
                        <td><label for="perm_<?php echo $key; ?>"><?php echo $name; ?></label></td>
                    </tr>
                <?php endforeach; ?>
            </table>

            <h3>Password</h3>

            <p>
                The password will only be changed if you enter something in this box.
            </p>

            <div class="fieldwrapper">
                <label for="password">Password</label>
                <div class="field">
                    <input type="text" name="password" id="password" />
                </div>
            </div>

            <div class="buttonwrapper">
                <button type="submit">Save changes</button>
            </div>

            </form>

        </div>

        <div class="col_clear">&nbsp;</div>
    </div>
</body>
</html>
