<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

if (!array_key_exists("admin", $_SESSION)) {
    header('Location: index.php');
    exit;
}

if (!array_key_exists("id", $_GET)) {
    exit;
}

// Get general zone info
$stmt = $conn->prepare("SELECT * FROM zone WHERE zone_id = ?");
$stmt->execute(array($_GET["id"]));
$zone = $stmt->fetch(PDO::FETCH_ASSOC);
$stmt->closeCursor();

// Get zone users and roles
$sqlUsers  = "SELECT zone_user.*, player.*, property_value color FROM zone_user ";
$sqlUsers .= "INNER JOIN player ON user_id = player.player_id ";
$sqlUsers .= "INNER JOIN player_property ON player_property.player_id = player.player_id "
           . "AND property_key = 'color' ";
$sqlUsers .= "WHERE zone_id = ? ORDER BY user_perm";

$stmt = $conn->prepare($sqlUsers);
$stmt->execute(array($zone["zone_id"]));
$users = $stmt->fetchAll(PDO::FETCH_ASSOC);
$stmt->closeCursor();

// Get lot and owner info
$stmt = $conn->prepare("SELECT * FROM zone_lot WHERE zone_id = ?");
$stmt->execute(array($zone["zone_id"]));
$lots = $stmt->fetchAll(PDO::FETCH_ASSOC);

$sqlLotUsers  = "SELECT zone_lotuser.*, player.*, property_value color FROM zone_lotuser ";
$sqlLotUsers .= "INNER JOIN player ON user_id = player_id ";
$sqlLotUsers .= "LEFT JOIN player_property ON player_property.player_id = player.player_id "
              . "AND property_key = 'color' ";
$sqlLotUsers .= "WHERE lot_id = ?";
$stmt = $conn->prepare($sqlLotUsers);
foreach ($lots as &$lot) {
    $stmt->execute(array($lot["lot_id"]));

    $lot["users"] = $stmt->fetchAll(PDO::FETCH_ASSOC);
}

// Get zone coordinates
$stmt = $conn->prepare("SELECT rect_x1, rect_y1, rect_x2, rect_y2 FROM zone_rect WHERE zone_id=?");
$stmt->execute(array($zone["zone_id"]));
$rects = $stmt->fetchAll(PDO::FETCH_ASSOC);
$stmt->closeCursor();

$stmt = $conn->prepare("SELECT name, x, y, z FROM warps WHERE warps.x >= ? AND warps.x <= ? AND warps.z >= ? AND warps.z <= ? AND warps.world = ?");
$warps = array();
foreach ($rects as $rect) {
    // Get warps within zone
    // Note: In the zone table, rect_y is actually the z coordinate in the warps table
    if ($rect['rect_x1'] > $rect['rect_x2']) {
        $qX1 = $rect['rect_x2'];
        $qX2 = $rect['rect_x1'];
    }
    else {
        $qX1 = $rect['rect_x1'];
        $qX2 = $rect['rect_x2'];
    }
    if ($rect['rect_y1'] > $rect['rect_y2']) {
        $qY1 = $rect['rect_y2'];
        $qY2 = $rect['rect_y1'];
    }
    else {
        $qY1 = $rect['rect_y1'];
        $qY2 = $rect['rect_y2'];
    }
    $stmt->execute(array($qX1, $qX2, $qY1, $qY2, $zone["zone_world"]));
    $warps += $stmt->fetchAll(PDO::FETCH_ASSOC);
}

?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Tregmine Admin Tool</title>
    <style type="text/css"> @import 'style.css';
    </style>
</head>
<body>
    <div id="layout_wrapper">
        <h1 id="banner"><span>Tregmine Admin Tool</span></h1>

        <?php require 'menu.php'; ?>

        <h2 class="info">Zone Information: <?php echo $zone["zone_name"]; ?> (<?php echo $zone["zone_id"]; ?>)</h2>

        <div class="col50">

            <table class="info">
                <tr>
                    <th colspan=2 class="infoHeader">Actions</th>
                </tr>
                <tr><td width=100%>&nbsp;</td><td>&nbsp;</td></tr>
            </table>

            <ul>
                <?php if (array_key_exists("senioradmin", $_SESSION)): ?>
                <?php endif; ?>
            </ul>

            <table class="info">
                <tr>
                    <th colspan=2 class="infoHeader">Info</th>
                </tr>
                <tr>
                    <th width=50%>Attribute</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>World</th>
                    <td><?php echo $zone["zone_world"]; ?></td>
                </tr>
                <tr>
                    <td>Coordinates</th>
                    <td>
                    <?php foreach ($rects as $rect): ?>
                        <?php echo "(".($rect['rect_x1'] ? $rect['rect_x1'] : 'NULL').",".($rect['rect_y1'] ? $rect['rect_y1'] : 'NULL').") - (".($rect['rect_x2'] ? $rect['rect_x2'] : 'NULL').",".($rect['rect_y2'] ? $rect['rect_y2'] : 'NULL').")"; ?><br />
                    <?php endforeach; ?>
                    </td>
                </tr>
                <tr>
                    <td>Main Owner</th>
                    <td><?php echo $zone["zone_owner"] ? $zone["zone_owner"] : 'NULL'; ?></td>
                </tr>
                <tr>
                    <td colspan=2>&nbsp;</td>
                </tr>
                <tr>
                    <td>Enter Default</th>
                    <td><?php echo $zone["zone_enterdefault"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <td>Place Blocks Default</th>
                    <td><?php echo $zone["zone_placedefault"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <td>Destroy Blocks Default</th>
                    <td><?php echo $zone["zone_destroydefault"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <td>PVP</th>
                    <td><?php echo $zone["zone_pvp"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <td>Hostiles</th>
                    <td><?php echo $zone["zone_hostiles"] ? "yes" : "no"; ?></td>
                </tr>
                <tr>
                    <td>Enter Message</th>
                    <td><?php echo $zone["zone_entermessage"]; ?></td>
                </tr>
                <tr>
                    <td>Exit Message</th>
                    <td><?php echo $zone["zone_exitmessage"]; ?></td>
                </tr>
                <tr>
                    <td>Texture</th>
                    <td><?php echo $zone["zone_texture"]; ?></td>
                </tr>

            </table>

            <table class="info">
                <tr>
                    <th colspan=3 class="infoHeader">Members</th>
                </tr>
                <tr>
                    <!-- <th>ID</th> -->
                    <th width=100%>Name</th>
                    <th>Rank</th>
                    <th>Action</th>
                </tr>
                <?php 
                foreach ($users as $user): ?>
                    <tr>
                        <!-- <td><?php echo $user["player_id"]; ?></td> -->
                        <td>
                        <?php 
                        $playerColor = $user["color"];
                        if (!$playerColor) {
                            $playerColor = "No Color / Tourist";
                        }
                        $playerColor = $colors[$playerColor];
                        echo "<a class=\"$playerColor\" href=\"search.php?q=" . $user["player_name"] . "\">" . $user["player_name"] . "</a></td>\n";
                        ?>
                        <td><?php echo $user["user_perm"]; ?></td>
                        <td></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        </div>

        <div class="col50">
            <table class="info">
                <cols>
                    <col />
                    <col style="width: 50px;" />
                </cols>
                <tr>
                    <th colspan="2" class="infoHeader">Warps</th>
                </tr>
                <tr>
                    <th>Name</th>
                    <th>(x,y,z)</th>
                </tr>
                <?php foreach($warps as $warp): ?>
                    <tr>
                        <td><?php echo $warp['name']; ?></td>
                        <td>(<?php printf("%d, %d, %d", $warp['x'], $warp['y'], $warp['z']); ?>)</td>
                    </tr>
                <?php endforeach; ?>
            </table>

            <table class="info">
                <tr>
                    <th colspan=2 class="infoHeader">Lots</th>
                </tr>
                <tr>
                    <th width=100%>Name</th>
                    <th>Owners</th>
                </tr>
                <?php foreach ($lots as $lot): ?>
                <tr>
                    <td> <?php echo $lot["lot_name"]; ?></td>
                    <td>
                    <?php foreach($lot["users"] as $lVal): 
                        $playerColor = $lVal["color"];
                        if (!$playerColor) {
                            $playerColor = "No Color / Tourist";
                        }
                        $playerColor = $colors[$playerColor]; ?>
                        <a class="<?php echo $playerColor; ?>" 
                           href="search.php?q=<?php echo $lVal["player_name"]; ?>">
                           <?php echo $lVal["player_name"]; ?></a><br>
                    <?php endforeach; ?>
                    </td>
                </tr>
                <?php endforeach; ?>
                </table>
            </div>
        <div class="col_clear">&nbsp;</div>
    </div>
</body>
</html>
