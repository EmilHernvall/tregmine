<?php

require_once '_init.php';
require_once '_check.php';
require_once '_perm.php';
require_once '_password.php';

if (!array_key_exists("senioradmin", $_SESSION)) {
    header('Location: index.php');
    exit;
}

if (!array_key_exists("id", $_GET)) {
    header('Location: start.php');
    exit;
}

$id = $_GET["id"];

$color = $_POST["color"];

$newPerms = $_POST["perm"];

// lookup player
$stmt = $conn->prepare("SELECT * FROM player WHERE player_id = ?");
$stmt->execute(array($_GET["id"]));

$player = $stmt->fetch();

$stmt->closeCursor();

// generate a string of all permissions and remove them for this user
$cleanStr = implode(",", array_map(function($a) { return "'".$a."'"; }, array_keys($permissionList)));
$cleanStr .= ",'color'";

$stmt = $conn->prepare("DELETE FROM player_property WHERE property_key IN (" . $cleanStr . ") AND player_id = ?");
$stmt->execute(array($id));

// insert new permissions
$stmt = $conn->prepare("INSERT INTO player_property VALUES (?, ?, ?, null)");

foreach ($newPerms as $perm => $v) {
    $stmt->execute(array($id, $perm, "true" ));
}

// insert new color
$stmt->execute(array($id, "color", $color));

// change password
$password = $_POST["password"];

if ($password) {
    $encrypted = crypt($password, "$5$" . gensalt() . "$");

    $stmt = $conn->prepare("UPDATE player SET player_password = ? WHERE player_id = ?");
    $stmt->execute(array($encrypted, $id));
}

//$conn->commit();

header('Location: player_perm.php?id='.$id);
