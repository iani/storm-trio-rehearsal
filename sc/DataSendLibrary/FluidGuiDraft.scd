//:
var window;
var cellspec, viscosityspec, dtspec, fadespeedspec;
var sliderMaker;
var send;

cellspec = ControlSpec (30, 250, \lin, 1, 100);
viscosityspec = ControlSpec (0, 0.003, \lin, 0, 0.0015);
dtspec = ControlSpec (0, 2.5, \lin, 0, 1.75);
fadespeedspec = ControlSpec (0, 0.995, \lin, 0, 0.5);

send = { | val | postf ("sending: %\n", val) };

sliderMaker = { | name = "test", min = 0, max = 1, step = 0, default = 0.5, message = \test |
	var slider, numbox, value, spec;
	spec = ControlSpec (min, max, \lin, step, default);
	HLayout (
		StaticText ().string_ (name),
		slider = Slider ()
		.orientation_ ('horizontal')
		.value_ (spec.unmap (default))
		.action_ ({ | me |
			value = spec.map (me.value);
			numbox.value = value;
			send.(value);
		}),
		numbox = NumberBox ()
		.maxDecimals_ (5)
		.value_ (default)
	);
};

window = Window ("fluids");
window.layout = VLayout (
	sliderMaker.("num cells", 30, 250, 1, 100),
	HLayout (
		StaticText ().string_ ("draw fluids"),
		CheckBox ()
	),
	HLayout (
		StaticText ().string_ ("draw particles"),
		CheckBox ()
	),
	sliderMaker.("viscosity", 0, 0.003, 0, 0.001),
	sliderMaker.("dt", 0, 0.25, 0, 0.175)
);

window.front;