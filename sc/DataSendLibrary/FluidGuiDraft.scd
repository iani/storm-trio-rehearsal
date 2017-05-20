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
		numbox = NumberBox ().value_ (default)
	);
};

window = Window ("fluids");
window.layout = VLayout (
	sliderMaker.("num cells", ),
	HLayout (
		StaticText ().string_ ("num cells"),
		Slider ().orientation_ ('horizontal'),
		NumberBox ().value_ (10)
	),
	HLayout (
		StaticText ().string_ ("draw fluids"),
		CheckBox ()
	),
	HLayout (
		StaticText ().string_ ("draw particles"),
		CheckBox ()
	),
	HLayout (
		StaticText ().string_ ("viscosity"),
		Slider ().orientation_ ('horizontal')
	),
	HLayout (
		StaticText ().string_ ("dt"),
		Slider ().orientation_ ('horizontal')
	),
	HLayout (
		StaticText ().string_ ("fade speed"),
		Slider ().orientation_ ('horizontal')
	));
window.front;