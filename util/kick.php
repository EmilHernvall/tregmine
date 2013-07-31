<?php

$subjectId = 1;
$issuerId = 2;
$message = "Testing, testing.";

$key = "PO1BBUS3NEB9MR9CPW1X5AQE84B0QM1W";
$host = "localhost:9192";
$path = "/playerkick";
$query = sprintf("subjectId=%d&issuerId=%d&message=%s",
                 $subjectId,
                 $issuerId,
                 urlencode($message));

$signingKey = $path;
$url = "http://" . $host . $path;
if ($query) {
    $signingKey .= "?" . $query;
    $url .= "?" . $query;
}

$signingKey = base64_encode(hash_hmac("sha1", $signingKey, $key, true));

$headers = array("Authorization: " . $signingKey);

$curl = curl_init();
curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
$data = curl_exec($curl);

var_dump($data);
