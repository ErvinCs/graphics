#include <iostream>
#include <GL/glew.h>
#include <GL/freeglut.h>

void displayCallback();


int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB);

	glutInitWindowPosition(200, 100);
	glutInitWindowSize(500, 500);

	glutCreateWindow("Test");

	glutDisplayFunc(displayCallback);
	glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

	glutMainLoop();

	return 0;
}

void displayCallback()
{
	glClear(GL_COLOR_BUFFER_BIT);
	glLoadIdentity();

	// Draw calls here

	glFlush();
}