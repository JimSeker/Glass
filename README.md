Google Glass Example code
=====
HelloGlass is a vey very very simple glass app to get started.

CardDemo is using there static Card and scrollviewcard so show some pictures and text.  also using the tap listeners to update one card (maybe not be 100% way to do it, but it works...)

GlassInvaders is based on the AndGame in https://github.com/JimSeker/drawing repo.   It has been updated to the screen size,
using glass guesture and a demo of voice (but so slow to use in the game).

OpenGLDemo is based on the BouncyCube  https://github.com/JimSeker/opengl repo.  There is also a demo of a simple menu for exit the app.  This is a openGl 1.1 version.

LiveCardOpenGL is using there LiveCards and the opnGL must use 2.0 version.  has a simple menu to stop the service as required.

LiveCardGameStat  using the LiveCard with a low frequency update.  

LiveCard GameStat uses the LiveCardGameStat, but adds the text to speech to read the scores.  Also add the IPC (ibinder) stuff so the menuActivity can call be to the service to do the work.

SensorDemo shows some of the hardware sensors: LightSensor, RotationVectorSensor, Accelerator, and Magnetic Sensor.  The data is displayed to the screen.

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course. All examples are for google glass and android.
