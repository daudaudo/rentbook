package app;

import core.Facades.Router;
import java.util.HashMap;
import app.Models.Users;
import app.Render.Address;
import app.Render.Book;
import app.Render.Cart;
import app.Render.Category;
import app.Render.Heart;
import app.Render.Home;
import app.Render.Search;
import app.Render.UserInfo;
import core.Facades.FormManager;

public class Routes {
    public static void register(){

        Router.belong().set("home",(req)->{
            
            return ()->{
                FormManager.belong().setMainContentPane("main", "home");
                new Home().render(FormManager.belong().getView("main"),req);
            };
        });

        Router.belong().set("heart",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "heart");
                new Heart().render(FormManager.belong().getView("main"),req);
            };
        }).middleware("auth");

        Router.belong().set("cart",(req)->{
            
            return ()->{
                FormManager.belong().setMainContentPane("main", "cart");
                new Cart().render(FormManager.belong().getView("main"),req);
            };
        }).middleware("auth");

        Router.belong().set("userinfo",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "userinfo");

                new UserInfo().render(FormManager.belong().getView("main"),req);
            };
        }).middleware("auth");

        Router.belong().set("login",(req)->{
            FormManager.belong().setMainContentPane("main", "login");
            return null;
        });

        Router.belong().set("login_post",(req)->{
            HashMap<String,Object> user = new Users().findWhere(req.input("user"), "password = '"+req.input("pass")+"'");
            if(user==null) return ()->{
                FormManager.belong().showAlert("Sai tài khoản hoặc mật khẩu");
            };

            else{
                req.session().add("user", req.input.get("user"));
                req.session().add("pass", req.input.get("pass"));

                return ()->{
                    Router.belong().route("home");
                };
            }
        });

        Router.belong().set("signin",(req)->{
            FormManager.belong().setMainContentPane("main", "signin");
            return null;
        });

        Router.belong().set("signin_post", (req)->{
            req.session().add("user", req.input.get("user"));
            req.session().add("pass", req.input.get("pass"));

            HashMap<String,Object> user = new HashMap<>();
            user.put("username",req.input.get("user"));
            user.put("password",req.input.get("pass"));
            user.put("email",req.input.get("email"));
            
            new Users().insert(user);

            req.update();

            return ()->{
                Router.belong().route("home");
            };
        });

        Router.belong().set("logout",(req)->{
            req.session().clear();
            req.update();
            return ()->{
                Router.belong().route("home");
            };
        });

        Router.belong().set("search",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "search");
                new Search().render(FormManager.belong().getView("main"),req);
            };
        });

        Router.belong().set("category",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "search");
                new Category().render(FormManager.belong().getView("main"),req);
            };
        });
        
        Router.belong().set("book",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "book");
                new Book().render(FormManager.belong().getView("main"),req);
            };
        });
        

        Router.belong().set("address",(req)->{
            return ()->{
                FormManager.belong().setMainContentPane("main", "address");
                new Address().render(FormManager.belong().getView("main"),req);
            };
        });
    }
}
