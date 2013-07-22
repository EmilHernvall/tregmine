<?php
require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';

$sql  = "SELECT player_report.*, a.player_name issuer, "
      . "b.player_name subject FROM player_report ";
$sql .= "INNER JOIN player a ON a.player_id = issuer_id ";
$sql .= "INNER JOIN player b ON b.player_id = subject_id ";
$sql .= "ORDER BY report_timestamp DESC ";
$sql .= "LIMIT 100";

$stmt = $conn->prepare($sql);
$stmt->execute(array());

$reports = $stmt->fetchAll();
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

        <h2 class="info">Reports</h2>

        <table class="info">
            <cols>
                <col style="width: 150px;" />
                <col style="width: 150px;" />
                <col style="width: 75px;" />
                <col style="width: 125px;" />
                <col style="width: 125px;" />
            </cols>
            <tr>
                <th>Issuer</th>
                <th>Subject</th>
                <th>Action</th>
                <th>Timestamp</th>
                <th>Valid Until</th>
            </tr>
            <?php foreach ($reports as $report): ?>
                <tr>
                    <td style="font-style: italic;"><?php echo $report["issuer"]; ?></td>
                    <td style="font-style: italic;">
                        <a href="player_report.php?id=<?php echo $report["subject_id"]; ?>"><?php echo $report["subject"]; ?></a>
                    </td>
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
                </tr>
                <tr>
                    <td style="padding: 10px; border-bottom: 1px dotted #fff;" colspan="5"><?php echo $report["report_message"]; ?></td>
                </tr>
            <?php endforeach; ?>
        </table>
    </div>
</body>
</html>
