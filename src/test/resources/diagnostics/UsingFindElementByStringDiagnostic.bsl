Функция ПростоФункция(Строка1)
    Возврат Неопределено;
КонецФункции

Процедура ТочкаВхода()

    // Сработает
    Должность = Справочники.Должности.найтиПонаименованию("Ведущий бухгалтер");
    // Сработает. Вообще это не компилируемый код
    Должность2 = Справочники.Должности2.НайтиПоНаименованию();
    // Не сработает
    Должность3 = ПростоФункция("Ведущий бухгалтер");
    // Сработает
    Справочники.Должности4.НайтиПоНаименованию("Бухгалтер");

КонецПроцедуры

Процедура ТочкаВхода2()

    // Пока не сработает
    Наименование = "Рога и Копыта";
    Значение = Справочники.Организации.НайтиПоНаименованию(Наименование);

    // Сработает
    Значение2 = Справочники.Валюты.НайтиПоКоду("777");

    // Сработает
    Значение2 = Справочники.Валюты.НайтиПоКоду(777);

КонецПроцедуры