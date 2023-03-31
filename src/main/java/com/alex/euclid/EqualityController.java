package com.alex.euclid;


import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Класс Equality наследует класс View.
 * Класс контролер для equality.fxml. Предназначен для вывода вспомогательного окна с доказательствами трех признаков
 * равенства треугольников.
 *
 * @author A. Nosov
 * @version 1.0
 */
public class EqualityController extends View{

    @FXML
    public Group groupTriangles;
    public Button bntCombine;
    public Text namePr, proof, theorem, proposition_1, proposition_2;
    public Arc arcA1;
    public Arc arcA;
    public Line hatch2_2;
    public Line hatch2_1;
    public Arc arcB1_1;
    public Arc arcB1_2;
    public Polygon treangle1;
    public Group groupTr2;
    public Group groupTr3;
    public Group hatchB, hatchB1;
    public Text txtC1;
    public Text txtA1;
    public Text poindC;
    public Text poindB1;
    public Text poindB;
    @FXML
    private Arc arcB_1, arcB_2;
    @FXML
    private Line hatch_1, hatch_2;

    /**
     * Метод initialize().
     * Предназначен для инициализации переменных.
     */
    @FXML
    private void initialize() {
        //1 признак равенства
        if (model.getWindShow() == 0) {
            arcB_1.setVisible(false);
            arcB_2.setVisible(false);
            arcB1_1.setVisible(false);
            arcB1_2.setVisible(false);
            arcA1.setVisible(true);
            arcA.setVisible(true);
            hatch_1.setVisible(true);
            hatch_2.setVisible(true);
            hatch2_1.setVisible(true);
            hatch2_2.setVisible(true);
            hatchB.setVisible(false);
            hatchB1.setVisible(false);
            poindC.setVisible(true);
            namePr.setText("Первый признак равенства треугольников.");
            model.setTextX(220);
            model.setTextY(30);
            //Передать для вывода в View
            model.setTextGo(namePr);
            model.notifyObservers("TextGo");
            proof.setText("Теорема (признак равенства треугольников по двум сторонам и углу между ними.)");
            model.setTextGo(proof);
            model.notifyObservers("TextGo");
            theorem.setText("Если две стороны и угол между ними одного треугольника соответственно равны двум сторонам и углу между ними другого треугольника, то такие треугольники равны.");
            model.setTextGo(theorem);
            model.notifyObservers("TextGo");
            proposition_1.setText("Рассмотрим треугольники АВС и А₁В₁С₁, у которых АВ=А₁В₁, АС=А₁С₁, углы А и А₁ равны. Докажем что  △АВС=△А₁В₁С₁");
            model.setTextGo(proposition_1);
            model.notifyObservers("TextGo");
            proposition_2.setText("Так как ∠А=∠А₁, то треугольник АВС можно наложить на треугольник А₁В₁С₁ так, что вершина А совместится с вершиной А₁, а стороны АВ и АС наложатся соответственно на лучи А₁В₁ и А₁С₁. Поскольку АВ=А₁В₁, АС=А₁С₁, то сторона АВ совместится со стороной А₁В₁, а сторона АС - со стороной А₁С₁ в частности совместятся точки В и В₁, С и С₁. Следовательно совместятся стороны ВС и В₁С₁. Итак треугольники АВС и А₁В₁С₁ полностью совместятся, значит, они равны. Теорема Доказана.");
            model.setTextGo(proposition_2);
            model.notifyObservers("TextGo");
        }
        //2 признак равенства
        if (model.getWindShow() == 1) {
            arcB_1.setVisible(true);
            arcB_2.setVisible(true);
            arcB1_1.setVisible(true);
            arcB1_2.setVisible(true);
            arcA1.setVisible(true);
            arcA.setVisible(true);
            hatch_1.setVisible(false);
            hatch_2.setVisible(false);
            hatch2_1.setVisible(false);
            hatch2_2.setVisible(false);
            hatchB.setVisible(false);
            hatchB1.setVisible(false);
            poindC.setVisible(true);
            namePr.setText("Второй признак равенства треугольников.");
            model.setTextX(220);
            model.setTextY(30);
            model.setTextGo(namePr);
            model.notifyObservers("TextGo");
            proof.setText("Теорема (признак равенства треугольников по стороне и прилежащим к ней углам.)");
            model.setTextGo(proof);
            model.notifyObservers("TextGo");
            theorem.setText("Если сторона и два прилежащих к ней угла одного треугольника соответственно равны стороне и двум прилежащим к ней углам другого треугольника, то такие треугольники равны.");
            model.setTextGo(theorem);
            model.notifyObservers("TextGo");
            proposition_1.setText("Рассмотрим треугольники АВС и А₁В₁С₁, у которых АВ=А₁В₁, ∠А=∠А₁, ∠B=∠B₁  равны. Докажем что  △АВС=△А₁В₁С₁");
            model.setTextGo(proposition_1);
            model.notifyObservers("TextGo");
            proposition_2.setText("Наложим треугольник АВС на треугольник А₁В₁С₁ так, чтобы вершина А совместилась с вершиной А₁, сторона АВ с равной ей стороной А₁В₁, а вершины С и С₁ оказались по одну сторону от прямой А₁В₁. Так как ∠А=∠А₁ и ∠В=∠В₁, то сторона АС наложится на луч А₁С₁, а сторона ВС - на луч В₁С₁. Поэтому вершина С -общая точка сторон АС и ВС - окажется лежащей как на луче А₁С₁, так и на луче В₁С₁ и, следовательно, совместятся с общей точкой этих лучей - вершиной С₁. Значит совместятся стороны АС и А₁С₁, ВС и В₁С₁. Итак треугольники АВС и А₁В₁С₁ полностью совместятся, поэтому они равны. Теорема доказана. ");
            model.setTextGo(proposition_2);
            model.notifyObservers("TextGo");
        }
        //3 признак равенства
        if (model.getWindShow() == 2) {
            arcB_1.setVisible(false);
            arcB_2.setVisible(false);
            arcB1_1.setVisible(false);
            arcB1_2.setVisible(false);
            arcA1.setVisible(false);
            arcA.setVisible(false);
            hatch_1.setVisible(true);
            hatch_2.setVisible(true);
            hatch2_1.setVisible(true);
            hatch2_2.setVisible(true);
            hatchB.setVisible(true);
            hatchB1.setVisible(true);
            namePr.setText("Третий признак равенства треугольников.");
            model.setTextX(220);
            model.setTextY(30);
            model.setTextGo(namePr);
            model.notifyObservers("TextGo");
            proof.setText("Теорема (признак равенства треугольников по трем сторонам.)");
            model.setTextGo(proof);
            model.notifyObservers("TextGo");
            theorem.setText("Если три стороны одного треугольника соответственно равны трем сторонам другого треугольника, то такие треугольники равны.");
            model.setTextGo(theorem);
            model.notifyObservers("TextGo");
            proposition_1.setText("Рассмотрим треугольники АВС и А₁В₁С₁, у которых АВ=А₁В₁, ВС=В₁С₁, СА=С₁А₁. Докажем что  △АВС=△А₁В₁С₁");
            model.setTextGo(proposition_1);
            model.notifyObservers("TextGo");
            proposition_2.setText("Так как по условию теоремы стороны АС и А₁С₁, ВС и В₁С₁ равны, то △А₁С₁С и △В₁С₁С - равнобедренные. По теореме о свойствах углов равнобедренного треугольника ∠1=∠2, ∠3=∠4, поэтому ∠А₁СВ₁=∠А₁С₁В₁. Итак АС=А₁С₁, ВС=В₁С₁, ∠С=∠С₁. Следовательно, треугольники АВС и А₁В₁С₁ равны по первому признаку равенства треугольников. Теорема доказана.");
        }
    }

    /**
     * Метод onClickCombine().
     * Предназначен для вывода анимации совмещения треугольников.
     */
    public void onClickCombine() {
        //Перемещениe для 3 признака равенства треугольников
        if (model.getWindShow() == 2) {
            //Перенос
            groupTr3.setVisible(false);
            TranslateTransition tr = new TranslateTransition(Duration.seconds(5.0), groupTriangles);
            tr.setFromX(0);
            tr.setFromY(0);
            tr.setToX(242);
            tr.setToY(65);
            tr.setCycleCount(1);
            tr.setInterpolator(Interpolator.LINEAR);
            //Вращение по оси Х
            RotateTransition rtX = new RotateTransition(Duration.seconds(5.0), groupTriangles);
            rtX.setAutoReverse(true);
            rtX.setAxis(Rotate.Y_AXIS);
            rtX.setFromAngle(0.0);
            rtX.setToAngle(180.0);
            rtX.setCycleCount(1);
            rtX.setInterpolator(Interpolator.LINEAR);
            //Вращение по оси Z
            RotateTransition rt2 = new RotateTransition(Duration.seconds(5.0), groupTr2);
            rt2.setAutoReverse(true);
            rt2.setAxis(Rotate.Z_AXIS);
            rt2.setFromAngle(0.0);
            rt2.setToAngle(-82.0);
            rt2.setCycleCount(1);
            rt2.setInterpolator(Interpolator.LINEAR);

            //Параллельное выполнение
            ParallelTransition pt = new ParallelTransition(groupTriangles);
            pt.getChildren().addAll(tr, rtX, rt2);
            pt.play();
            pt.setOnFinished(event -> {
                groupTr3.setVisible(true);
                poindC.setRotate(180);
                poindB.setRotate(180);
            });
        } else {
            //Перемещениe для 1 и 2 признаков равенства треугольников
            TranslateTransition tr = new TranslateTransition(Duration.seconds(5.0), groupTriangles);
            tr.setFromX(0);
            tr.setFromY(0);
            tr.setToX(284);
            tr.setToY(20);
            tr.setCycleCount(1);
            tr.setInterpolator(Interpolator.LINEAR);
            //Поворот
            RotateTransition rt2 = new RotateTransition(Duration.seconds(5.0), groupTriangles);
            rt2.setAutoReverse(true);
            rt2.setFromAngle(0.0);
            rt2.setToAngle(-30.0);
            rt2.setCycleCount(1);
            rt2.setInterpolator(Interpolator.LINEAR);
            //Параллельное выполнение
            ParallelTransition pt = new ParallelTransition(groupTriangles);
            pt.getChildren().addAll(tr, rt2);
            pt.play();
        }
    }
}

