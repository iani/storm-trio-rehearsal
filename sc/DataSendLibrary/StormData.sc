/*
Load magnetic storm data from data folder and send them, in various ways.

*/
StormData {
	classvar <dataPaths;
	classvar <data;
	classvar <xmax, <xmin, <ymax, <ymin, <zmax, <zmin;
	classvar <>addresses;
	
	*loadData {
		var path, x, y, z;
		this.init;
		path = PathName (this.filenameSymbol.asString).pathOnly ++ "data/";
		path.postln;
		dataPaths = (path ++ "*.dat").pathMatch;
		dataPaths.postln;
		postf ("% starts loading data from %\n", this, path);
		data = this.loadDataAsPoints (dataPaths.first);
		postf ("% finished loading data from %\n", this, path);
		postf ("% data rows read\n", data.size + 1);
		#x, y, z = data.flop;
		xmax = x.maxItem;
		xmin = x.minItem;
		ymax = y.maxItem;
		ymin = y.minItem;
		zmax = z.maxItem;
		zmin = z.minItem;
	}

	*init {
		if (addresses.size == 0) {
			addresses = [NetAddr ("127.0.0.1", 12345)]
		};
	}

	*loadDataAsPoints { | path |
		// select only these rows which contain 7 columns exactly:
		^CSVFileReader.read(path).select ({ | row |
			row.size == 7;
		}).flop[2..4].flop collect: { | row |
			row collect: { | string |
				string.replace("+", "").interpret;
			}
		};
	}

	*playData { | rate = 0.02 ... argAddresses  |
		if (argAddresses.size == 0) {
			argAddresses = argAddresses;
		};
		{
			var triplet, start, stop;
			triplet = [];
			start = Process.elapsedTime.postln;
			StormData.data do: { | row |
				triplet = triplet ++ row;
				if (triplet.size == 9) {
					addresses do: { | addr |
						addr.sendMsg ('/vertex', *(triplet.postln ++ Array.rand (3, 0.1, 0.9)));
					};
					triplet = [];
					rate.wait;
				};
			};
			stop = Process.elapsedTime.postln;
			(stop-start).postln;
		}.fork
	}
}