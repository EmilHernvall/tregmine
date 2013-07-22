<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

if (!array_key_exists("admin", $_SESSION)) {
    header('Location: index.php');
    exit;
}

$players = array_key_exists("players", $_GET) ? $_GET["players"] : "";
$start = array_key_exists("start", $_GET) ? $_GET["start"] : "";
$text = array_key_exists("text", $_GET) ? $_GET["text"] : "";
$channel = array_key_exists("channel", $_GET) ? $_GET["channel"] : "";

$params = array();

$sql  = "SELECT * FROM player_chatlog ";
$sql .= "INNER JOIN player USING (player_id) ";
$sql .= "WHERE 1 ";
if ($players) {
    $players = explode(",", $players);
    $sql .= "AND (";
    $delim = "";
    foreach ($players as $player) {
        $sql .= $delim;
        $sql .= "player_name = ? ";
        $delim = "OR ";
        $params[] = $player;
    }
    $sql .= ") ";
} else {
    $players = array();
}
if ($start) {
    $sql .= "AND chatlog_timestamp >= ? ";
    $params[] = strtotime($start);
}
if ($text) {
    $sql .= "AND chatlog_message LIKE ? ";
    $params[] = $text;
}
if ($channel) {
    $sql .= "AND chatlog_channel = ? ";
    $params[] = $channel;
}
$sql .= "ORDER BY chatlog_timestamp DESC ";
$sql .= "LIMIT 1000";

$stmt = $conn->prepare($sql);
$stmt->execute($params);

$chatlogs = $stmt->fetchAll();
?>
<!DOCTYPE html>
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

        <h2 class="info">Chatlogs</h2>

        <form method="get" action="chatlog.php">
            <h3 class="formHeader">Filter</h3>

            <div class="field">
                <label for="players">Players (comma separated)</label>
                <div class="element">
                <input type="text" name="players" id="players" value="<?php echo implode(",", $players); ?>"/>
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="field">
                <label for="start">Start Date</label>
                <div class="element">
                <input type="text" name="start" id="start" value="<?php echo $start; ?>"/>
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="field">
                <label for="text">Text</label>
                <div class="element">
                    <input type="text" name="text" id="text" value="<?php echo $text; ?>" />
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="field">
                <label for="channel">Channel</label>
                <div class="element">
                    <input type="text" name="channel" id="channel" value="<?php echo $channel; ?>" />
                </div>
                <div class="end">&nbsp;</div>
            </div>

            <div class="button">
                <button type="submit">Filter</button>
            </div>
        </form>

        <h3 class="infoHeader">Logs</h3>
        <table class="info">
            <?php foreach ($chatlogs as $log): ?>
                <tr>
                    <td><?php echo $log["chatlog_channel"]; ?></td>
                    <td><?php echo $log["player_name"]; ?></td>
                    <td><?php echo date("Y-m-d H:i:s", $log["chatlog_timestamp"]); ?></td>
                    <td><?php echo $log["chatlog_message"]; ?></td>
                </tr>
            <?php endforeach; ?>
        </table>

    </div>
</body>
</html>
