public class Logica {
    double[] notas;
    private int totalEstudiantes;
    private int aprobados;
    private int reprobados;

    // Constructor que acepta un array de valores (notas)
    public Logica(double[] notas) {
        this.notas = notas;
    }

    // Método para calcular el promedio de las notas
    public double calcularPromedio() {
        double sumaNotas = 0;
        for (double nota : notas) {
            sumaNotas += nota;
        }
        return sumaNotas / notas.length;
    }
    
    // Constructor que acepta el total de estudiantes, aprobados y reprobados
    public Logica(int totalEstudiantes, int aprobados, int reprobados) {
        this.totalEstudiantes = totalEstudiantes;
        this.aprobados = aprobados;
        this.reprobados = reprobados;
    }
    
    // Método para determinar el resultado (aprobado o reprobado) basado en el promedio
    public String determinarResultado(double promedio) {
        if (promedio >= 30) {
            return "Aprobó";
        } else {
            return "Reprobó";
        }
    }

    // Método para calcular el porcentaje de aprobados
    public double calcularPorcentajeAprobados() {
        return (double) aprobados / totalEstudiantes * 100;
    }

    // Método para calcular el porcentaje de reprobados
    public double calcularPorcentajeReprobados() {
        return (double) reprobados / totalEstudiantes * 100;
    }
}
