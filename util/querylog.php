<?php

$key = "F67R4LNVSUG8SO8TQ07DXHDQC34NAU4I";
$host = "localhost:9192";
$path = "/querylog";
$query = "";

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
$data = json_decode(curl_exec($curl), true);

usort($data, function($a, $b) { return $a["count"] > $b["count"]; });

printf("count\tavg\tmax\tsql\n");
foreach ($data as $query) {
    printf("%d\t%d\t%d\t%s\n", $query["count"], $query["avg"], $query["max"], $query["sql"]);
}
