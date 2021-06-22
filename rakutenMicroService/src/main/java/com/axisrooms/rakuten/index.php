<?php

	$apiKey = "ppk_tCY1H50";
	$secret = "psk_tU7805RotTag2s4";
	$timestamp = time();
	$key = $apiKey.$secret.$timestamp;
	 
	$hash = hash_init('sha512', 1, $key);
	$signature = hash_final($hash);
	$authHeader = 'UCONNECT APIKey='.$apiKey.',Signature='.$signature.',Timestamp='.$timestamp;
	echo $authHeader;

?>
