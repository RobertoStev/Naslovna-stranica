import java.util.*;

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Vasiot kod ovde

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String category) {
        super(String.format("Category %s was not found", category));
    }
}

class Category implements Comparable<Category> {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Category o) {
        return this.getName().compareTo(o.getName());
    }
}

abstract class NewsItem {
    private String heading;
    private Date date;
    protected Category category;

    public NewsItem() {
        heading = "";
        date = null;
        category = null;
    }

    public NewsItem(String heading, Date date, Category category) {
        this.heading = heading;
        this.date = date;
        this.category = category;
    }

    public String getHeading() {
        return heading;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public abstract String getTeaser();
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String heading, Date date, Category category, String text) {
        super(heading, date, category);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getTeaser() {
        String textToPrint = null;
        if (text.length() < 80)
            textToPrint = text;
        else
            textToPrint = text.substring(0, 80);

        Date now = new Date();
        int minutes = (int) ((now.getTime() - getDate().getTime()) / 60 / 1000);

        return String.format("%s\n%d\n%s\n", getHeading(), minutes, textToPrint);
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    public MediaNewsItem(String heading, Date date, Category category, String url, int views) {
        super(heading, date, category);
        this.url = url;
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public int getViews() {
        return views;
    }

    public String getTeaser() {
        Date now = new Date();
        int minutes = (int) ((now.getTime() - getDate().getTime()) / 60 / 1000);

        return String.format("%s\n%d\n%s\n%d\n", getHeading(), minutes, url, views);
    }
}

class FrontPage {
    private List<NewsItem> newsItemList;
    private Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        newsItemList = new ArrayList<>();
    }

    public void addNewsItem(NewsItem newsItem) {
        newsItemList.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        List<NewsItem> listWithCategory = new ArrayList<>();
        for (NewsItem item : newsItemList) {
            if (item.getCategory().compareTo(category) == 0)//sporedba na objekti so comapreTo vo Category klasata
                listWithCategory.add(item);
        }
        return listWithCategory;
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        boolean hasCategory = false;
        for (Category c : categories) {
            if (c.getName().equals(category))
                hasCategory = true;
        }
        if (!hasCategory)
            throw new CategoryNotFoundException(category);

        List<NewsItem> listWithCategory = new ArrayList<>();
        for (NewsItem item : newsItemList) {
            if (item.category.getName().compareTo(category) == 0)//sporedba na stringovi so compareTo metodot
                listWithCategory.add(item);
        }
        return listWithCategory;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NewsItem item : newsItemList)
            sb.append(item.getTeaser());
        return sb.toString();
    }
}

