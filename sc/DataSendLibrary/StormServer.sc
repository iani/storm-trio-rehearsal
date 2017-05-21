// 20 May 2017 16:13
/*
Setup for playing the performance on the 38 channel system.

Gui for rewiring outputs
*/

StormServer {
	*boot {
		"This is StormServer booting !!!!!!!!!!!!!!!!".postln;
		"BOOTING SERVER".postln;
		Server.default.boot;
		"BOOTED SERVER !!!!!!!!!!!!!!!!".postln;
	}
}
