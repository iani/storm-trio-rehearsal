/*
Load magnetic storm data from data folder and send them, in various ways.

*/
StormData {
	classvar <dataPaths;
	classvar <data;
	classvar <xmax, <xmin, <ymax, <ymin, <zmax, <zmin;
	
	*loadData {
		var path, x, y, z;
		path = PathName (this.filenameSymbol.asString).pathOnly ++ "data/";
		path.postln;
		dataPaths = (path ++ "*.dat").pathMatch;
		dataPaths.postln;
		data = this.loadDataAsPoints (dataPaths.first);
		#x, y, z = data.flop;
		xmax = x.maxItem;
		xmin = x.minItem;
		ymax = y.maxItem;
		ymin = y.minItem;
		zmax = z.maxItem;
		zmin = z.minItem;
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

	*playData {
	  
	}
}