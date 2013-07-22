<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';
require_once '_items.php';

if (!array_key_exists("id", $_GET)) {
    exit;
}

$stmt = $conn->prepare("SELECT * FROM player WHERE player_id = ?");
$stmt->execute(array($_GET["id"]));

$player = $stmt->fetch();

$stmt->closeCursor();

$stmt = $conn->prepare("SELECT * FROM player_login WHERE player_id = ? ORDER BY login_timestamp");
$stmt->execute(array($_GET["id"]));

$logins = $stmt->fetchAll();

$sqlTransactions  = "SELECT * FROM player_transaction " 
                  . "INNER JOIN player ON player_id = recipient_id "
                  . "WHERE sender_id = ? ";
$sqlTransactions .= "UNION SELECT * FROM player_transaction "
                  . "INNER JOIN player ON player_id = sender_id "
                  . "WHERE recipient_id = ? ";
$sqlTransactions .= "ORDER BY transaction_timestamp";

$stmt = $conn->prepare($sqlTransactions);
$stmt->execute(array($_GET["id"], $_GET["id"]));

$transactions = $stmt->fetchAll();

$sql  = "SELECT * FROM inventory_item ";
$sql .= "INNER JOIN inventory USING (inventory_id) ";
$sql .= "WHERE inventory_type = 'player' AND player_id = ?";

$stmt = $conn->prepare($sql);
$stmt->execute(array($player["player_id"]));

$inventory = $stmt->fetchAll(PDO::FETCH_ASSOC);
$slots = array();
foreach ($inventory as $item) {
    $slots[$item["item_slot"]] = $item;
}
?>
<!DOCTYPE html>
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

        <h2 class="info"><?php echo $player["player_name"]; ?> (<?php echo $player["player_id"]; ?>)</h2>

        <div class="col75">

            <table class="info">
                <cols>
                    <col />
                    <col style="width: 50px;" />
                    <col style="width: 50px;" />
                </cols>
                <tr>
                    <th colspan="3" class="infoHeader">Logins</th>
                </tr>
                <tr>
                    <th>Timestamp</th>
                    <th>Action</th>
                    <th>Duration</th>
                </tr>
                <?php 
                $last = null;
                $total = 0;
                $loginCount = 0;
                foreach ($logins as $i => $row):
                    $duration = "";
                    if ($row["login_action"] == "login") {
                        $last = $row;
                        $loginCount++;
                    }
                    else if ($row["login_action"] == "logout") {
                        if ($last != null) {
                            $dur = $row["login_timestamp"] - $last["login_timestamp"];
                            $total += $dur;
                            $min = $dur / 60;
                            $sec = $dur % 60;
                            $duration = sprintf("%02d:%02d", $min, $sec);
                        } else {
                            $duration = "N/A";
                        }
                    }
                    if ($i > 10) {
                        continue;
                    }
                    ?>
                    <tr>
                        <td><?php echo date("Y-m-d H:i:s", $row["login_timestamp"]); ?></td>
                        <td><?php echo ucfirst($row["login_action"]); ?></td>
                        <td><?php echo $duration; ?></td>
                    </tr>
                <?php endforeach; ?>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>
                        <div>
                            <span style="font-weight: bold;">Total time:</span>
                            <?php
                            $hours = $total / 3600;
                            $min = ($total % 3600) / 60;
                            $sec = $total % 60;
                            printf("%02d:%02d:%02d", $hours, $min, $sec);
                            ?>
                        </div>
                        <div>
                            <span style="font-weight: bold;">Total logins:</span>
                            <?php printf("%d", $loginCount); ?>
                        </div>
                    </td>
                </tr>
            </table>


            <table class="info">
                <tr>
                    <th colspan="4" class="infoHeader">Transactions</th>
                </tr>
                <tr>
                    <th>Sender</th>
                    <th>Receiver</th>
                    <th>Timestamp</th>
                    <th>Amount</th>
                </tr>
                <?php 
                foreach ($transactions as $row): ?>
                    <tr>
                        <td>
                            <?php 
                            if ($row["sender_id"] == $player["player_id"]) {
                                echo $player["player_name"]; 
                            } else {
                                echo $row["player_name"]; 
                            }
                            ?>
                        </td>
                        <td>
                            <?php 
                            if ($row["recipient_id"] == $player["player_id"]) {
                                echo $player["player_name"]; 
                            } else {
                                echo $row["player_name"]; 
                            }
                            ?>
                        </td>
                        <td><?php echo date("Y-m-d H:i:s", $row["transaction_timestamp"]); ?></td>
                        <td><?php echo $row["transaction_amount"]; ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>

        </div>

        <div class="col25">

            <h3 class="actionsHeader">Actions</h3>
            <ul class="actions">
                <li><a href="player_report.php?id=<?php echo $player["player_id"]; ?>">Reports</a></li>
                <?php if (array_key_exists("senioradmin", $_SESSION)): ?>
                <li><a href="player_perm.php?id=<?php echo $player["player_id"]; ?>">Permissions</a></li>
                <?php endif; ?>
            </ul>

        </div>

        <div class="col_clear">&nbsp;</div>

        <table class="info">
            <tr>
                <th colspan="9" class="infoHeader">Inventory</th>
            </tr>

            <?php for ($x = 0; $x < 4; $x++): ?>
            <tr>
                <?php for ($y = 0; $y < 9; $y++): ?>
                    <td style="width: 11%; height: 75px; border: 1px solid #000; text-align: center; font-size: 10px;">
                        <?php
                        if (array_key_exists($x*9+$y, $slots)) {
                            $slot = $slots[$x*9+$y];
                            $item = $slot["item_material"];
                            if ($item < 133) {
                                echo '<div><img src="blocks/' . $item . '.png" width="50" height="50" /></div>';
                            } else {
                                echo '<div style="height: 50px;">' . 
                                    implode(" ", array_map("ucfirst", array_map("strtolower", explode("_", $items[$item])))) . '</div>';
                            }
                            echo '<div>' . $slot["item_count"] . '</div>';
                        } else {
                            echo "&nbsp;";
                        }
                        ?>
                    </td>
                <?php endfor; ?>
            </tr>
            <?php endfor; ?>
        </table>

    </div>
</body>
</html>
