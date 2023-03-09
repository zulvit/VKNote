# VKNote
<h1>Приложение для аудио-заметок</h1>
  <label>Реализованные функции</label>
  <ul>
    <li><input type="checkbox">Запись замток в память на локальном устройстве</li>
    <li><input type="checkbox">RecyclerView для отображения вех заметок</li>
    <li><input type="checkbox">Таймер для отслеживания включения записи заметок</li>
    <li><input type="checkbox">Удаление заметок свайпом влево или вправо по экрану</li>
    <li><input type="checkbox">FloatingActionBar для начала и остановки записи</li>
    <li><input type="checkbox">Диалоговое окно с возможностью переименования файла, сохранения и отмены записи</li>
    <li><input type="checkbox">Авторизация при помощи VK SDK(в процессе)</li>
  </ul>
    <label>Предстоит сделать</label>
  <ul>
    <li><input type="checkbox">Сохранение в базе данных при помощи Room database</li>
    <li><input type="checkbox">Добавление анимации при включении записи диктофоно с помощью Canvas</li>
    <li><input type="checkbox">Сохранение заметок у пользователя в документах в VK</li>
    <li><input type="checkbox">Языковая модель, обученная на TensorFlow и переведённая в lite-формат</li>
    <li><input type="checkbox">Добавление мультиязычности</li>
  </ul>
  
  ![Снимок экрана 2023-03-10 001339](https://user-images.githubusercontent.com/98654361/224161526-347f0c2c-fda6-4147-9563-41377ed1f1d6.png)
  
  <h3>Подключённые зависимости:</h3>
<code>implementation 'com.vk:android-sdk-core:3.5.1'
implementation 'com.vk:android-sdk-api:3.5.1' // generated models and api methods
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.8.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation "androidx.core:core-ktx:1.9.0"
implementation 'androidx.annotation:annotation:1.3.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.4.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1'
</code>
