<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

if (!array_key_exists("id", $_GET)) {
    exit;
}

$stmt = $conn->prepare("SELECT * FROM zone WHERE zone_id = ?");
$stmt->execute(array($_GET["id"]));

$zone = $stmt->fetch(PDO::FETCH_ASSOC);

$stmt->closeCursor();

$stmt = $conn->prepare("SELECT * FROM zone_user INNER JOIN player ON user_id = player_id WHERE zone_id = ?");
$stmt->execute(array($zone["zone_id"]));

$users = $stmt->fetchAll(PDO::FETCH_ASSOC);

$sql  = "SELECT zone_lot.*, GROUP_CONCAT(player_id SEPARATOR ', ') player_ids, "
      . "GROUP_CONCAT(player_name SEPARATOR ', ') player_names FROM zone_lot ";
$sql .= "INNER JOIN zone_lotuser USING (lot_id) ";
$sql .= "INNER JOIN player ON player_id = zone_lotuser.user_id ";
$sql .= "WHERE zone_id = ? GROUP BY lot_id";

$stmt = $conn->prepare($sql);
$stmt->execute(array($zone["zone_id"]));

$lots = $stmt->fetchAll(PDO::FETCH_ASSOC);

?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Tregmine Admin Tool</title>
    <style type="text/css"> @import 'style.css';
    </style>
</head>
<body>
    <div id="layout_wrapper">
        <h1>Tregmine Admin Tool</h1>

        <?php require 'menu.php'; ?>

        <h2><?php echo $zone["zone_name"]; ?> (<?php echo $zone["zone_id"]; ?>)</h2>

        <div class="col50">

            <h3>Actions</h3>

            <ul>
                <?php if (array_key_exists("senioradmin", $_SESSION)): ?>
                <?php endif; ?>
            </ul>

            <h3>Info</h3>

            <table>
                <tr>
                    <th>ID</th>
                    <td><?php echo $zone["zone_id"]; ?></td>
                </tr>
                <tr>
                    <th>Name</th>
                    <td><?php echo $zone["zone_name"]; ?></td>
                </tr>
                <tr>
                    <th>Enter Default</th>
                    <td><?php echo $zone["zone_enterdefault"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <th>Place Blocks Default</th>
                    <td><?php echo $zone["zone_placedefault"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <th>Destroy Blocks Default</th>
                    <td><?php echo $zone["zone_destroydefault"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <th>PVP</th>
                    <td><?php echo $zone["zone_pvp"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <th>Hostiles</th>
                    <td><?php echo $zone["zone_hostiles"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <th>Enter Message</th>
                    <td><?php echo $zone["zone_entermessage"]; ?></td>
                </tr>
                <tr>
                    <th>Exit Message</th>
                    <td><?php echo $zone["zone_exitmessage"]; ?></td>
                </tr>
                <tr>
                    <th>Texture</th>
                    <td><?php echo $zone["zone_texture"]; ?></td>
                </tr>
                <tr>
                    <th>Main Owner</th>
                    <td><?php echo $zone["zone_owner"]; ?></td>
                </tr>
            </table>

            <h3>Lots</h3>

            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Owners</th>
                </tr>
                <?php foreach ($lots as $lot): ?>
                    <tr>
                        <td><?php echo $lot["lot_id"]; ?></td>
                        <td><?php echo $lot["lot_name"]; ?></td>
                        <td><?php echo $lot["player_names"]; ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>

        </div>

        <div class="col50">

            <h3>Members</h3>

            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Rank</th>
                    <th>Action</th>
                </tr>
                <?php 
                foreach ($users as $user): ?>
                    <tr>
                        <td><?php echo $user["player_id"]; ?></td>
                        <td><?php echo $user["player_name"]; ?></td>
                        <td><?php echo $user["user_perm"]; ?></td>
                        <td></td>
                    </tr>
                <?php endforeach; ?>
            </table>

        </div>

        <div class="col_clear">&nbsp;</div>
    </div>
</body>
</html>
