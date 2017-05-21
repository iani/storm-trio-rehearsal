/*
	Load magnetic storm data from data folder and send them, in various ways.

*/
StormData {
	classvar <dataPaths;
	classvar <data;
	classvar <xmax, <xmin, <ymax, <ymin, <zmax, <zmin;
	classvar <>addresses;
	classvar <>localOfAddr;
	
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
		("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!").postln;
		#x, y, z = data.flop;
		xmax = x.maxItem;
		xmin = x.minItem;
		ymax = y.maxItem;
		ymin = y.minItem;
		zmax = z.maxItem;
		zmin = z.minItem;
	}

	*init {
		localOfAddr = NetAddr ("127.0.0.1", 12345);
		if (addresses.size == 0) {
			addresses = [localOfAddr];
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
			StormData.data do: { | row, index |
				triplet = triplet ++ row;
				if (index % 10 == 0) { postf ("played row: %\n", index + 1) };
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

	*fluidsGui {
		var window;
		window = Window ("fluids");
		window.layout = VLayout (
			this.makeSlider("num cells", 30, 250, 1, 100),
			HLayout (
				StaticText ().string_ ("draw fluids"),
				CheckBox ()
			),
			HLayout (
				StaticText ().string_ ("draw particles"),
				CheckBox ()
			),
			this.makeSlider("viscosity", 0, 0.003, 0, 0.001),
			this.makeSlider("dt", 0, 0.25, 0, 0.175)
		);
		window.front;
	}

	*modelGui {
		var window;
	}

	*makeSlider { | name = "test", min = 0, max = 1, step = 0, default = 0.5, param = \test |
		var slider, numbox, value, spec;
		spec = ControlSpec (min, max, \lin, step, default);
		^HLayout (
			StaticText ().string_ (name),
			slider = Slider ()
			.orientation_ ('horizontal')
			.value_ (spec.unmap (default))
			.action_ ({ | me |
				value = spec.map (me.value);
				numbox.value = value;
				this.send2OF(param, value);
			}),
			numbox = NumberBox ()
			.maxDecimals_ (5)
			.value_ (default)
		);
	}

	*send2OF { | param = \test ... values |
		localOfAddr.sendMsg (param, *values);
		postf ("sent % value: %\n", param, values);
	}
}