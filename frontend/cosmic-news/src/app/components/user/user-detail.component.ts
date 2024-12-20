import { Component, ElementRef, HostListener } from '@angular/core';
import { User } from '../../models/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { Me } from '../../models/me.model';
import { MessageService } from '../../services/message.service';
import { News } from '../../models/news.model';
import { Picture } from '../../models/picture.model';
import { PaginationService } from '../../services/pagination.service';
import { Badge } from '../../models/badge.model';
@Component({
    selector: "user-detail",
    templateUrl: './user-detail.component.html',
    styleUrls: ['../../styles/data.css', '../../styles/profile.css', '../../styles/cards.css']
})
export class UserDetailComponent {
    me:Me;
    user: User;
    edit:boolean;
    typeUser:string;
    admin: boolean;
    otherUser: boolean;
    editUser:string;
    image:boolean | undefined;
    imageUser: string | undefined;
    newsList: News[] = [];
    pictures: Picture[] = [];
    badges: Badge[] = [];

    last_page_news: number = 0;
    hasMore_news: boolean = true;

    last_page_pictures: number = 0;
    hasMore_pictures: boolean = true;
    cardWidth: number = 250 + 2 * 20;
    rowElements: number;
    canAdd: boolean = false;
    idUser:number;
    profileUrl:string;

    constructor(activatedRoute: ActivatedRoute,
        private router:Router,
        private userService: UserService,
        private pagService: PaginationService,
        private messageService: MessageService,
        private elementRef: ElementRef) {

        const isFront = window.location.pathname.includes('front');
        this.profileUrl = isFront ? '/images/noPhotoUser.jpg' : '/api/images/users';

        let id = activatedRoute.snapshot.params['id'];
        const routeSegments = activatedRoute.snapshot.url;
        this.user = {mail:"",pass:"",nick:"",roles:[]}
        this.image = false;
        this.otherUser = true;
        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
        }
        if (id) {
            this.idUser = id;
            userService.getUser(id).subscribe(
                response => {this.user = response as User;
                    this.userService.me().subscribe(
                        (response:any)=>{
                            this.otherUser = response.mail !== this.user.mail;
                            this.admin = response.mail === "admin";
                            },
                        (error:any) => {}
                    );
                    this.loadBadges();  
                    this.loadNews();
                    this.loadPictures();                                 
                    this.editUser='/users/edit/'+this.user.id
                    if (this.user.image){
                      this.image=true;
                    }else{
                      this.image=false;
                    }
                    if (this.image){
                        userService.getUserImage(this.user).subscribe(
                            response =>{ if(response){
                                const blob = new Blob([response], {type: 'image/jpeg'});
                                this.imageUser = URL.createObjectURL(blob);
                            }else{
                                this.imageUser = undefined;
                            }
                        },
                        error => {this.imageUser = undefined;}
                        );
                    }
                },
                error => this.messageService.showFatalError("Usuario no encontrado")
            );
        } 
    }

    ngOnInit(): void {
        this.updateRowElements();
    }

    //Obsolet
    showImageUser() {
        return this.user.image ? '/api/lifeguards' + this.user.id + '/photoUser' : '/assets/images/noPhotoUser.jpg';
    }

    deleteUser(){
        this.userService.deleteUser(this.user).subscribe(
            _ => this.router.navigate(['/']),
            error => console.error(error)
        )
    }

    logout(){
        this.userService.logout().subscribe(
            _ => this.router.navigate(['login']),
            error => console.error(error)
        );
    }

    loadBadges(){
      this.userService.getUserBadges(this.idUser).subscribe(
        (badges: Badge[]) => { 
          this.badges.push(...badges);
        },
        (error) => {
          console.log(error);
        }
      );
    }

    loadNews() {
        this.userService.getUserNews(this.idUser, this.last_page_news, this.rowElements).subscribe(
          (newsList: News[]) => {
            if (!newsList) {
              this.hasMore_news = false;
              return;
            }
    
            for (let news of newsList) {
              this.pagService.getNewsImage(news).subscribe(
                (image) => {
                  news.image = URL.createObjectURL(image);
                },
                (error) => {
                  console.log(error);
                }
              );
            }
    
            this.newsList.push(...newsList);
          },
          (error) => {
            console.log(error);
            this.hasMore_news = false;
          }
        );
    
        this.last_page_news++;
    
        this.userService.getUserNews(this.idUser,this.last_page_news, this.rowElements).subscribe(
          (newsList: News[]) => {
            if (!newsList)
              this.hasMore_news = false;
          },
          (error) => {
            console.log(error);
            this.hasMore_news = false;
          }
        );
      }

      loadPictures() {
        this.userService.getUserPictures(this.idUser, this.last_page_pictures, this.rowElements).subscribe(
          (pictures: Picture[]) => {
            if (!pictures) {
              this.hasMore_pictures = false;
              return;
            }
    
            for (let picture of pictures) {
              this.pagService.getPictureImage(picture).subscribe(
                (image) => {
                    picture.image = URL.createObjectURL(image);
                },
                (error) => {
                  console.log(error);
                }
              );
            }
    
            this.pictures.push(...pictures);
          },
          (error) => {
            console.log(error);
            this.hasMore_pictures = false;
          }
        );
    
        this.last_page_pictures++;
    
        this.userService.getUserPictures(this.idUser,this.last_page_pictures, this.rowElements).subscribe(
          (pictures: Picture[]) => {
            if (!pictures)
              this.hasMore_pictures = false;
          },
          (error) => {
            console.log(error);
            this.hasMore_pictures = false;
          }
        );
      }
    
      private updateRowElements() {
        const cards = this.elementRef.nativeElement.querySelector('.cards');
        const total = Math.floor(cards.offsetWidth / this.cardWidth);
        this.rowElements = Math.max(2, Math.min(5, total));
      }
    
    @HostListener('window:resize', ['$event'])
      onResize() {
        this.updateRowElements();
      }

}