<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

$player = array();
$settings = array();
if (array_key_exists("id", $_GET)) {
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
        <h1>Tregmine Admin Tool</h1>

        <?php require 'menu.php'; ?>

        <h2><?php echo $player["player_name"]; ?> (<?php echo $player["player_id"]; ?>)</h2>

        <div class="col50">

            <h3>Actions</h3>

            <ul>
                <li><a href="player_report.php?id=<?php echo $player["player_id"]; ?>">Reports</a></li>
                <li><a href="player_perm.php?id=<?php echo $player["player_id"]; ?>">Permissions</a></li>
            </ul>

            <h3>Logins</h3>

            <table>
                <tr>
                    <th>Timestamp</th>
                    <th>Action</th>
                    <th>Duration</th>
                </tr>
                <?php 
                $last = null;
                foreach ($logins as $row): ?>
                    <tr>
                        <td><?php echo date("Y-m-d H:i:s", $row["login_timestamp"]); ?></td>
                        <td><?php echo ucfirst($row["login_action"]); ?></td>
                        <td>
                            <?php
                            if ($row["login_action"] == "login") {
                                $last = $row;
                            }
                            else if ($row["login_action"] == "logout") {
                                if ($last != null) {
                                    $dur = $row["login_timestamp"] - $last["login_timestamp"];
                                    $min = $dur / 60;
                                    $sec = $dur % 60;
                                    printf("%02d:%02d", $min, $sec);
                                } else {
                                    echo "N/A";
                                }
                            }
                            ?>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </table>

        </div>

        <div class="col50">

            <h3>Transactions</h3>

            <table>
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

        <div class="col_clear">&nbsp;</div>
    </div>
</body>
</html>
