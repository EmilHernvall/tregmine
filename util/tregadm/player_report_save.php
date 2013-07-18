<?php

require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';
require_once '_password.php';

$do = array_key_exists("do", $_GET) ? $_GET["do"] : "report";

if ($do == "report") {
    if (!array_key_exists("id", $_GET)) {
        header('Location: start.php');
        exit;
    }

    $id = $_GET["id"];

    $action = $_POST["action"];
    $duration = array_key_exists("duration", $_POST) ? $_POST["duration"] : "";
    $text = $_POST["text"];

    $duration = $duration ? strtotime($duration) : null;

    if (!array_key_exists("admin", $_SESSION)) {
        $action = "comment";
    }

    if ($action == "comment") {
        $duration = null;
    }

    $sql  = "INSERT INTO player_report (subject_id, issuer_id, report_action, "
          . "report_message, report_timestamp, report_validuntil) ";
    $sql .= "VALUES (?, ?, ?, ?, ?, ?)";

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($id, $_SESSION["id"], $action, $text, time(), $duration));
}
else if ($do == "cancel") {
    if (!array_key_exists("admin", $_SESSION)) {
        header('Location', 'index.php');
        exit;
    }

    if (!array_key_exists("reportid", $_GET)) {
        header('Location: start.php');
        exit;
    }

    $reportId = $_GET["reportid"];

    $sql = "SELECT * FROM player_report WHERE report_id = ?";

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($reportId));

    $report = $stmt->fetch(PDO::FETCH_ASSOC);

    $stmt->closeCursor();

    $type = $report["report_action"] == "ban" ||
            $report["report_action"] == "softwarn" ||
            $report["report_action"] == "hardwarn";

    if (!$report || !$type) {
        header('Location: index.php');
        exit;
    }

    $id = $report["subject_id"];

    $sql  = "UPDATE player_report SET report_validuntil = null ";
    $sql .= "WHERE report_id = ?";

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($reportId));

    $text = ucfirst($report["report_action"]) . " cancelled.";

    $sql  = "INSERT INTO player_report (subject_id, issuer_id, report_action, "
          . "report_message, report_timestamp, report_validuntil) ";
    $sql .= "VALUES (?, ?, ?, ?, ?, ?)";

    $stmt = $conn->prepare($sql);
    $stmt->execute(array($report["subject_id"], $_SESSION["id"], "comment", $text, time(), null));
}

header('Location: player_report.php?id='.$id);
