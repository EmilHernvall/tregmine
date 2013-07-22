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

    $sql  = "SELECT * FROM player_report ";
    $sql .= "INNER JOIN player ON player_id = issuer_id ";
    $sql .= "WHERE subject_id = ? ";
    $sql .= "ORDER BY report_timestamp DESC";

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($_GET["id"]));

    $reports = $stmt->fetchAll();
}
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

        <h2 class="info"><?php echo $player["player_name"]; ?> (<?php echo $player["player_id"]; ?>)</h2>

        <div class="col75">
            <form method="post" action="player_report_save.php?id=<?php echo $player["player_id"]; ?>">

                <h3 class="formHeader">New Report</h3>

                <div class="field">
                    <label for="action">Action</label>
                    <div class="element">
                        <select name="action" id="action">
                            <option value="comment">Comment</option>
                            <?php if (array_key_exists("admin", $_SESSION)): ?>
                            <option value="softwarn">Softwarn</option>
                            <option value="hardwarn">Hardwarn</option>
                            <option value="ban">Ban</option>
                            <?php endif; ?>
                        </select>
                    </div>
                    <div class="end">&nbsp;</div>
                </div>

                <?php if (array_key_exists("admin", $_SESSION)): ?>
                <div class="field">
                    <label for="duration">Duration</label>
                    <div class="element">
                        <input type="text" id="duration" name="duration" />
                    </div>
                    <div class="end">&nbsp;</div>
                </div>
                <?php endif; ?>

                <div class="field">
                    <label for="text">Message</label>
                    <div class="element">
                        <textarea id="text" name="text"></textarea>
                    </div>
                    <div class="end">&nbsp;</div>
                </div>

                <div class="button">
                    <button type="submit">Submit</button>
                </div>

            </form>
        </div>

        <div class="col25">
            <h3 class="actionsHeader">Actions</h3>

            <ul class="actions">
                <?php if (array_key_exists("senioradmin", $_SESSION)): ?>
                <li><a href="player_perm.php?id=<?php echo $player["player_id"]; ?>">Permissions</a></li>
                <?php endif; ?>
                <li><a href="player_stats.php?id=<?php echo $player["player_id"]; ?>">Stats</a></li>
            </ul>
        </div>

        <div class="col_clear">&nbsp;</div>

        <h3 class="infoHeader">History</h3>
        <table style="width: 100%;" class="info">
            <cols>
                <col />
                <col style="width: 75px;" />
                <col style="width: 125px;" />
                <col style="width: 125px;" />
                <col style="width: 125px;" />
            </cols>

            <tr>
                <th>Issuer</th>
                <th>Action</th>
                <th>Timestamp</th>
                <th>Valid Until</th>
                <th>Action</th>
            </tr>
            <?php foreach ($reports as $report): ?>
                <tr>
                    <td style="font-style: italic;"><?php echo $report["player_name"]; ?></td>
                    <td style="font-style: italic;"><?php echo ucfirst($report["report_action"]); ?></td>
                    <td style="font-style: italic;"><?php echo date("Y-m-d H:i:s", $report["report_timestamp"]); ?></td>
                    <td style="font-style: italic;">
                        <?php
                        if ($report["report_validuntil"]) {
                            echo date("Y-m-d H:i:s", $report["report_validuntil"]);
                        } else {
                            echo "N/A";
                        }
                        ?>
                    </td>
                    <td style="font-style: italic;">
                        <?php
                        $type = $report["report_action"] == "ban" ||
                                $report["report_action"] == "softwarn" ||
                                $report["report_action"] == "hardwarn";
                        $admin = array_key_exists("admin", $_SESSION);
                        $valid = $report["report_validuntil"] > time();
                        if ($type && $admin && $valid): ?>
                            <a href="player_report_save.php?do=cancel&reportid=<?php echo $report["report_id"]; ?>" onclick="return confirm('Are you sure?');">Cancel</a>
                        <?php endif; ?>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px dotted #fff;" colspan="5">
                        <?php echo nl2br($report["report_message"]); ?>
                    </td>
                </tr>
            <?php endforeach; ?>
        </table>
    </div>
</body>
</html>
