<?php

function gensalt()
{
    $data = "abcdefghijklmnopqrstuvwxyz0123456789";
    $buf = "";
    for ($i = 0; $i < 16; $i++) {
        $buf .= $data[mt_rand(0, strlen($data))];
    }
    
    return $buf;
}
