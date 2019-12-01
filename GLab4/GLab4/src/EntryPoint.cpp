#include <iostream>
#include <GL/glew.h>
#include <GL/freeglut.h>

float xPos = -9;
bool xDirRight = true;
const float xStep = 0.2;

void displayCallback()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();	// Reset the parameters of the model-view matrix 

	// Translation - cannot be called in glBegin-glEnd
	glTranslatef(xPos, xPos, 0.0f);

	// GL_FLAT specifies that blending will not occur
	// GL_SMOOTH is the default
	glShadeModel(GL_SMOOTH);	

	// Draw calls
	glBegin(GL_POLYGON);
	
	// Specify Vertices - anti-clockwise fashion
	glColor3f(0.0f, 0.0f, 1.0f);
	glVertex2f(-1.0f,	   1.0f);
	glVertex2f(-1.0f,	  -1.0f);
	glColor3f(1.0f, 0.0f, 0.0f);
	glVertex2f(1.0f, -1.0f);
	glVertex2f(1.0f,  1.0f);

	glEnd();

	glutSwapBuffers();
}

// Called on window reshape
void reshapeCallback(int width, int height)
{
	// Set Viewport
	glViewport(0, 0, (GLsizei)width, (GLsizei)height);

	// Set Projection
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();	// Reset the parameters of the projection matrix 

	gluOrtho2D(-10, 10, -10, 10);
	glMatrixMode(GL_MODELVIEW); 
}

void timer(int)
{
	// Also call Display 60 times in 1 second - OpenGL does it by itself
	glutPostRedisplay();
	// Timer calls itself periodically - 60fps
	glutTimerFunc(1000 / 60, timer, 0);

	// Update X
	switch (xDirRight)
	{
	case true:
		if (xPos < 9)
			xPos += xStep;
		else
			xDirRight = false;
		break;
	case false:
		if (xPos > -9)
			xPos -= xStep;
		else
			xDirRight = true;
		break;
	}
	std::cout << "Timer-Step: xPos=" << xPos << std::endl;
}

void initColor()
{
	// Color is a state variable in OpenGL
	// If Color = Value, then everything drawn will be drawn in Color (which is Value at the given point in time)
	// Colors are assigned per vertex
	// Example: To draw a cube with different colors for faces, the color must be set before drawing each face
	glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
}

int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	// RGB & Double-Buffer display mode
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);

	glutInitWindowPosition(200, 100);
	glutInitWindowSize(500, 500);

	glutCreateWindow("Test");

	glutDisplayFunc(displayCallback);
	glutReshapeFunc(reshapeCallback);
	
	// Call a function after a specified ammount of time
	glutTimerFunc(1000, timer, 0);

	initColor();

	glutMainLoop();
}
