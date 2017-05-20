
NetAddr ("127.0.0.1", 12345).sendMsg ('/data', 50, 105, 50, 50);


//: LOCAL
{
	var addr;
	addr = NetAddr ("127.0.0.1", 12345);
	loop {
		addr.sendMsg ('/data', *({ 500.rand } ! 4));
		0.1.wait;
	}
}.fork;

//: DIMITRIS 19 May 2017 22:29
{
	var addr;
	addr = NetAddr ("192.168.2.89", 12345);
	loop {
		addr.sendMsg ('/data', *({ 500.rand } ! 4));
		0.1.wait;
	}
}.fork;

//: