package ru.miklelolyandex.officeexercises;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Exercise {
    public long id;
    public long exercise_set_id;
    @PrimaryKey
    @android.support.annotation.NonNull
    public String text;


    public Exercise (long exercise_set_id, long id, String text){
        this.id = id;
        this.exercise_set_id =exercise_set_id;
        this.text = text;
    }

    public String getText(){
        return this.text;
    }

    public static Exercise[] populateData() {
        return new Exercise[] {
                new Exercise(1,1, "Положите локти на стол. Затем поместите ладони рук на глаза. Закройте глаза и посмотрите во тьму."),
                new Exercise(1,2, "Стойте или сидите вертикально. Смотрите прямо вперед. Не двигая головой, смотрите влево. Сосредоточьтесь на том, что видидте. Потом посмотрите направо. Сосредоточься на том, что видишь."),
                new Exercise(1,3, "Сядьте прямо и посмотрите прямо вперед. Затем посмотрите вверх и сосредоточьтесь на том, что вы видите. Смотреть вниз. Не бойтесь морщить лоб или хмурить брови. Это полезно для глаз."),
                new Exercise(1,4, "Смотри прямо вперед. Посмотрите вниз и налево. Затем двигайте глазами по диагонали и посмотрите вверх и вправо. Сосредоточься на том, что видишь. Затем посмотрите прямо и выполните то же упражнение, глядя вниз и вправо, а затем глядя вверх и влево."),
                new Exercise(1,5, "Сядьте прямо и расслабьтесь. Посмотрите влево и медленно вращайте глазами по кругу по часовой стрелке. Для этого смотрите вверх, затем медленно двигайте глазами по часовой стрелке и смотрите вниз, а затем двигайте глазами и смотрите вправо. Сначала по чаовой, затем против."),
                new Exercise(1,6, "Сосредоточьтесь на близком объекте, например, на карандаше. Он должен быть на расстоянии 20-30 см от глаз. Затем посмотри на что-нибудь далекое. Сосредоточьтесь на этом далеком объекте и попытайтесь увидеть его в деталях. Затем снова посмотрите на близлежащий объект."),
                new Exercise(1,7, "Сядьте прямо. Смотрите вперед. Посмотрите на точку между бровями. Сосредоточьтесь на том, что вы видите, и посмотрите на несколько секунд. Потом перед собой. Затем снова посмотрите на точку между бровями."),
                new Exercise(1,8, "Сидеть прямо. Смотри прямо вперед. Смотреть на кончик носа в течение нескольких секунд. Затем снова смотрите вперед."),
                new Exercise(1,9, "Закройте глаза. Плотно закройте глаза. Держите их закрытыми в течение 2-3 секунд. Расслабьте мышцы глаз. Снова закройте глаза."),
                new Exercise(1,10, "Закройте глаза. Осторожно коснитесь ваших век и массируйте глаза круговыми движениями. Слегка нажимая. Не пытайтесь давить слишком сильно."),
                new Exercise(2,1, "Сядьте прямо обеими ногами на пол и руками на бедра или на стол. Держите шею прямо. Поднимите затылок вверх. Подбородок слегка опущен вниз, но не прижимайте его к груди. Расслабьте свои плечи. Почувствуйте растяжку в шее. Повторить несколько раз."),
                new Exercise(2,2, "Сядьте прямо обеими ногами на пол. Держите плечи назад и расслабьте руки. Держите шею прямо и смотрите вперед. Аккуратно поверните голову влево насколько это удобно. Ваш взгляд должен быть параллелен полу. Держите эту позицию. Затем вернитесь в исходное положение. Повернуть голову вправо насколько это удобно. Повторите несколько раз в каждом направлении."),
                new Exercise(2,3, "Сядьте прямо обеими ногами на пол. Держите плечи назад и расслабьте руки. Наколните левое ухо к левому плечу, насколько это удобно. Не сгибайте шею и не двигайте голову вперед или назад. Держите плечи опущенными. Не поднимайте плечи до ушей. Вернитесь в исходное положение и выполните это упражнение для правой стороны. Повторить несколько раз."),
                new Exercise(2,4, " Сядьте прямо. Опустите подбородок вниз к шее, насколько это удобно, сохраняя спину и шею вертикальными и прямыми. Не открывайте рот. Займите самое низкое положение. Затем вернитесь в исходное положение и опустите подбородок вместе с шеей. Прижмите подбородок к груди. Удерживайте нижнюю позицию в течение нескольких секунд и вернитесь в исходное положение. Повторите эти два типа сгибания несколько раз."),
                new Exercise(2,5, "Сядьте прямо обеими ногами на пол. Плечи назад, расслабьте руки. Держите шею прямо и смотрите вперед. Держа спину прямо, двигайте головой назад и поднимайте подбородок к потолку. Нижняя часть подбородка должна идти вертикально вверх, не открывая рот. Удерживая верхнюю позицию, вернитесь в исходное положение. Повторить несколько раз."),
                new Exercise(2,6, "Круговые движения головы в каждую сторону. Если у вас есть проблемы с шеей, то вы должны посоветовать с вашим доктором. Люди, страдающие от болей в спине, остеохондроза, частых головных болей или болей в шее, следует избегать полных кругов. Рисование восьмерки (\"8\") было бы лучшим выбором для них."),
                new Exercise(2,7, "Сядьте прямо. Держите плечи назад и расслабьте руки. Опустите голову. Шея должна быть параллельна полу. Удерживая эту позицию, поверните голову сначала в одну сторону, затем в другую. Повторить несколько раз.")
        };
    }
}