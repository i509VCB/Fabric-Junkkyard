package me.i509.junkkyard.schem;

class UnimplementedException extends RuntimeException {
	UnimplementedException(String className, String method) {
		super("An implementation of " + className + " with method " + method + " has not been implemented");
	}
}
