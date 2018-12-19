package apple1417.misc;

public class ComplexNumber {
    private double re;
    private double im;
    public ComplexNumber(double re, double im) {
        this.re = re;
        this.im = im;
    }
    public ComplexNumber(double mod, double arg, boolean isRadians) {
        if (!isRadians) {
            arg = Math.toRadians(arg);
        }
        re = mod*Math.cos(arg);
        im = mod*Math.sin(arg);
    }

    public ComplexNumber clone() {
        return new ComplexNumber(re, im);
    }

    public boolean equals(ComplexNumber z) {
        return re == z.re() && im == z.im();
    }

    public String toString() {
        return String.format("%f %s%fi", re, im >= 0 ? "+" : "", im);
    }

    public double re() {
        return re;
    }
    public double im() {
        return im;
    }
    public double mod() {
        return Math.sqrt(re*re + im*im);
    }
    public double arg() {
        return Math.atan(im/re);
    }

    public void setRe(double re) {
        this.re = re;
    }
    public void setIm(double im) {
        this.im = im;
    }
    public void setMod(double mod) {
        double arg = arg();
        re = mod*Math.cos(arg);
        im = mod*Math.sin(arg);
    }
    public void setArg(double arg) {
        double mod = mod();
        re = mod*Math.cos(arg);
        im = mod*Math.sin(arg);
    }

    public ComplexNumber add(ComplexNumber z) {
        return new ComplexNumber(re + z.re(), im + z.im());
    }
    public ComplexNumber add(double re, double im) {
        return new ComplexNumber(this.re + re, this.im + im);
    }
    public ComplexNumber add(double mod, double arg, boolean isRadians) {
        if (!isRadians) {
            arg = Math.toRadians(arg);
        }
        return new ComplexNumber(re + mod*Math.cos(arg), im + mod*Math.sin(arg));
    }

    public ComplexNumber mult(ComplexNumber z) {
        return new ComplexNumber(mod() * z.mod(), arg() + z.arg(), true);
    }
    public ComplexNumber mult(double re, double im) {
        return new ComplexNumber(this.re * re - this.im * im, this.re * im + this.im * re);
    }
    public ComplexNumber mult(double mod, double arg, boolean isRadians) {
        if (!isRadians) {
            arg = Math.toRadians(arg);
        }
        return new ComplexNumber(mod() * mod, arg() + arg, true);
    }

    public ComplexNumber inverse() {
        double denom = re*re + im*im;
        return new ComplexNumber(re/denom, -im/denom);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(re, im * -1);
    }

    public ComplexNumber ln() {
        return new ComplexNumber(Math.log(mod()), arg());
    }
    public ComplexNumber log(double base) {
        return ln().mult(new ComplexNumber(1/Math.log(base), 0));
    }
    public ComplexNumber exp(double base) {
        return new ComplexNumber(Math.pow(base, re), Math.log(Math.pow(base, im)), true);
    }
    public ComplexNumber exp() {
        return exp(Math.E);
    }

    public ComplexNumber sin() {
        return new ComplexNumber(Math.sin(re)*Math.cosh(-im), -Math.cos(re)*Math.sinh(-im));
    }
    public ComplexNumber cos() {
        return new ComplexNumber(Math.cos(re)*Math.cosh(-im), Math.sin(re)*Math.sinh(-im));
    }
    public ComplexNumber tan() {
        return sin().mult(cos().inverse());
    }

    public ComplexNumber sinh() {
        return exp().add(mult(-1, 0).exp().mult(-1, 0)).mult(0.5, 0);
    }
    public ComplexNumber cosh() {
        return exp().add(mult(-1, 0).exp()).mult(0.5, 0);
    }
    public ComplexNumber tanh() {
        return sinh().mult(cosh().inverse());
    }
}
