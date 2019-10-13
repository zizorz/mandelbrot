import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import models.MandelbrotInput;
import models.Complex;
import models.ImagePart;
import models.ImageWork;

class ImageWorkDivider {

    Flowable<ImageWork> DivideWork(MandelbrotInput input) {
        return Flowable.create(subscriber -> {
            var height = input.getHeight();
            var width = input.getWidth();

            var minC = input.getMinC();
            var maxC = input.getMaxC();

            var iterations = input.getMaxNrOfIterations();
            var divisions = input.getDivisions();

            var stepX = ((maxC.getReal() - minC.getReal()) / width);
            var stepY = ((maxC.getImaginary() - minC.getImaginary()) / height);


            var cReal = minC.getReal();
            var cImaginary = maxC.getImaginary();
            for (int i = 0; i < height; i+= divisions) {
                for (int j = 0; j < width; j+= divisions) {
                    var imageWork = new ImageWork();

                    var imagePart = new ImagePart();
                    var h = Math.min(divisions, height - i);
                    var w = Math.min(divisions, width - j);

                    imagePart.setTopLeftX(j);
                    imagePart.setTopLeftY(i);
                    imagePart.setHeight(h);
                    imagePart.setWidth(w);
                    imageWork.setImagePart(imagePart);

                    imageWork.setMinC(new Complex(cReal, cImaginary - stepY * h));
                    imageWork.setMaxC(new Complex(cReal + stepX * w, cImaginary));
                    imageWork.setMaxNrOfIterations(iterations);

                    subscriber.onNext(imageWork);

                    cReal += stepX * w;
                }
                cReal = minC.getReal();
                cImaginary -= stepY * Math.min(divisions, height - i);
            }
            subscriber.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

}
