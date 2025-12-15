# Achievement Tracker Frontend

React приложение для управления целями и достижениями.

## Технологии

- React 18
- TypeScript
- Vite
- React Router
- Zustand (state management)
- Axios (HTTP клиент)

## Установка

```bash
cd frontend
npm install
```

## Запуск

```bash
npm run dev
```

Приложение будет доступно по адресу http://localhost:3000

## Сборка

```bash
npm run build
```

Собранные файлы будут в папке `dist`

## Структура проекта

```
frontend/
├── src/
│   ├── components/      # Переиспользуемые компоненты
│   ├── pages/          # Страницы приложения
│   ├── services/       # API сервисы
│   ├── store/          # Zustand stores
│   ├── types/          # TypeScript типы
│   ├── App.tsx         # Главный компонент
│   └── main.tsx        # Точка входа
├── package.json
└── vite.config.ts
```

## API

Приложение использует прокси на `/api` для обращения к бэкенду. Бэкенд должен быть запущен на `http://localhost:8080`

## Основные страницы

- `/login` - Вход в систему
- `/register` - Регистрация
- `/goals` - Список целей
- `/goals/:id` - Детали цели
- `/groups` - Список групп
- `/groups/:id` - Детали группы
- `/notifications` - Уведомления и приглашения
- `/profile` - Профиль пользователя
- `/statistics` - Статистика
- `/admin` - Панель администратора

