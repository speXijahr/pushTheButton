package cz.etn.ptb;

import cz.etn.ptb.controllers.ButtonController;
import cz.etn.ptb.dbo.ButtonDBO;
import cz.etn.ptb.response.ButtonStateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class PtbApplicationTests {

	@Test
	void noHeartbeatTest() {
		ButtonDBO button = new ButtonDBO();
		button.setButtonId("1");

		Assert.isTrue(ButtonStateResponse.LightStatus.UNKNOWN.equals(ButtonStateResponse.getButtonState(button)));
	}

	@Test
	void oldHeartbeatTest() {
		ButtonDBO button = new ButtonDBO();
		button.setButtonId("1");
		button.setLastHeartbeat(Instant.now().minus(Duration.ofMinutes(10)));

		Assert.isTrue(ButtonStateResponse.LightStatus.UNKNOWN.equals(ButtonStateResponse.getButtonState(button)));
	}

	@Test
	void noReservationTest() {
		ButtonDBO button = new ButtonDBO();
		button.setButtonId("1");
		button.setLastHeartbeat(Instant.now());
		button.setReservationExpire(Instant.MIN);

		Assert.isTrue(ButtonStateResponse.LightStatus.LIGHTS_OFF.equals(ButtonStateResponse.getButtonState(button)));
	}

	@Test
	void blinkingTest() {
		ButtonDBO button = new ButtonDBO();
		button.setButtonId("1");
		button.setLastHeartbeat(Instant.now());
		button.setReservationExpire(Instant.now().plus(ButtonController.BUTTON_STATE_FLASHING_THRESHOLD).minus(Duration.ofSeconds(30)));

		Assert.isTrue(ButtonStateResponse.LightStatus.LIGHTS_BLINK.equals(ButtonStateResponse.getButtonState(button)));
	}

	@Test
	void expiredTest() {
		ButtonDBO button = new ButtonDBO();
		button.setButtonId("1");
		button.setLastHeartbeat(Instant.now());
		button.setReservationExpire(Instant.now().minus(Duration.ofMinutes(1)));

		Assert.isTrue(ButtonStateResponse.LightStatus.LIGHTS_OFF.equals(ButtonStateResponse.getButtonState(button)));
	}

	@Test
	void lightsOnTest() {
		ButtonDBO button = new ButtonDBO();
		button.setButtonId("1");
		button.setLastHeartbeat(Instant.now());
		button.setReservationExpire(Instant.now().plus(Duration.ofMinutes(7)));

		Assert.isTrue(ButtonStateResponse.LightStatus.LIGHTS_ON.equals(ButtonStateResponse.getButtonState(button)));
	}






}
