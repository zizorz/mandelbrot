class Complex {
    private float real;
    private float imaginary;

    Complex(float real, float imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    float getReal() {
        return real;
    }

    void setReal(float real) {
        this.real = real;
    }

    float getImaginary() {
        return imaginary;
    }

    void setImaginary(float imaginary) {
        this.imaginary = imaginary;
    }

    Complex square() {
        var newReal = real * real - imaginary * imaginary;
        var newImaginary = real * imaginary + real * imaginary;
        return new Complex(newReal, newImaginary);
    }

    Complex add(Complex c) {
        var newReal = this.real + c.real;
        var newImaginary = this.imaginary + c.imaginary;
        return new Complex(newReal, newImaginary);
    }

    float abs() {
        return (float) Math.sqrt((this.real * this.real) + (this.imaginary * this.imaginary));
    }

}
