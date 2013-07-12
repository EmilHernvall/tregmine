<?php

require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';
require_once '_password.php';

if (!array_key_exists("id", $_GET)) {
    header('Location: start.php');
    exit;
}

$id = $_GET["id"];
$text = $_POST["text"];

$sql  = "INSERT INTO player_report (subject_id, issuer_id, report_action, "
      . "report_message, report_timestamp, report_validuntil) ";
$sql .= "VALUES (?, ?, ?, ?, ?, null)";

$stmt = $conn->prepare($sql);
$stmt->execute(array($id, $_SESSION["id"], "comment", $text, time()));

header('Location: player_report.php?id='.$id);
