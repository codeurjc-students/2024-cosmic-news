import { Component, ViewChild } from '@angular/core';
import { User } from '../../models/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { HttpClient } from '@angular/common/http';
import { MessageService } from '../../services/message.service';

@Component({
    selector: "user-form",
    templateUrl: './user-form.component.html',
    styleUrls: ['../../styles/form.css']
})
export class UserFormComponent{
    user: User;
    id: number;
    edit:boolean;
    typeUser:string;
    mails:string[];
    nicks:string[];
    message:string;
    color:string;

    @ViewChild("file")
    file:any;

    constructor(activatedRoute: ActivatedRoute, private router:Router, private service: UserService, private httpClient: HttpClient, private messageService: MessageService) {
        this.id = activatedRoute.snapshot.params['id'];
        let type : string | undefined;
        const routeSegments = activatedRoute.snapshot.url;
        this.edit=false;

        this.user = {mail:"",pass:"",nick:"",roles:[]}

        this.service.getMails().subscribe(
            (response:any)=>{
                this.mails = response;
            },
            error=>{console.error(error)}
        )

        this.service.getNicks().subscribe(
            (response:any)=>{
                this.nicks = response;
            },
            error=>{console.error(error)}
        )

        if (routeSegments.length > 0) {
            const firstSegment = routeSegments[0];
            type = firstSegment.path;
        }
        if (this.id) {
            service.getUser(this.id).subscribe(
                user => {this.user = user
                    this.edit = true;
                },
                error => console.error(error)
            );
        }
    }

    save() {
        var message = this.checkForm()
        if (message !== ''){
            this.messageService.showError(message);
        }else if (this.color !== 'green' && !this.edit){
            this.messageService.showError("Email en uso por otro usuario.");
        }else{
            this.service.addOrUpdateUser(this.user).subscribe(
                (user: any) => {
                    if (this.edit) {
                        this.uploadUserImage(user);
                    } else {
                        this.messageService.showMessage("Usuario registrado correctamente.", "/");
                    }
                },
                _error => {
                    if (!this.edit)
                        this.messageService.showError("Email o nick en uso por otro usuario");
                }
            );
        }
    }

    private uploadUserImage(user: User): void {
        const image = this.file.nativeElement.files[0];
        if (image) {
          let formData = new FormData();
          formData.append("imageFile", image);
          this.service.setUserImage(user, formData).subscribe(
            response => {
                if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
                this.file.nativeElement.value = null;
                if (this.edit){
                    this.router.navigate(['/users/'+this.user.id])
                }else{
                    this.router.navigate(['/login']);
                }
            },
            error => {
                if (!this.edit){this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })};
                alert('Error uploading user image: ' + error)}
          );
        } else {
            if (!this.edit){
                //MIRAR ESTO
                const imageUrl = '/assets/images/noPhotoUser.jpg';
                fetch(imageUrl)
                .then(response => response.blob())
                .then(imageBlob => {
                    let formData = new FormData();
                    formData.append('imageFile', imageBlob, 'noPhotoUser.jpg');
                    this.service.setUserImage(user, formData).subscribe(
                    response => {
                        this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })
                        this.file.nativeElement.value = null;
                    },
                    error => {
                        this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })
                        console.error('Error uploading user image: ' + error);
                    }
                    );
                })
                .catch(error => {
                    this.httpClient.post('/api/auth/logout', { withCredentials: true }).subscribe((resp: any) => { })
                    console.error('Error downloading image:', error);
                });
            };
            if (this.edit){
                this.router.navigate(['/users/'+this.user.id])
            }else{
                this.router.navigate(['/login']);
            }
        }
      }   

    checkPassword(): void {
        const passInput: HTMLInputElement | null = document.getElementById("pass") as HTMLInputElement;
        if (passInput) {
            const pass: string = passInput.value;
            let message: string = "";
            let color: string = "";
    
            if (!pass) {
                message = "Introduzca una contraseña";
                color = "red";
            } else if (pass.length < 8) {
                message = "Contraseña débil";
                color = "orange";
            } else {
                message = "Contraseña segura";
                color = "green";
            }
            const messageDiv: HTMLElement | null = document.getElementById("passContent");
            if (messageDiv) {
                messageDiv.innerHTML = message;
                messageDiv.style.color = color;
            }
        }
    }
    
    checkMail(): void {
        const mailInput: HTMLInputElement | null = document.getElementById("mail") as HTMLInputElement;
        if (mailInput) {
            const mail: string = mailInput.value;
            if (this.mails.includes(mail)) {
                this.message = "Email no disponible";
                this.color = "red";
            }else if (mail==""){
                this.message = "Introduzca un mail";
                this.color = "red";
            }  else {
                this.message = "Email disponible";
                this.color = "green";
            }
            const messageDiv: HTMLElement | null = document.getElementById("mailContent");
            if (messageDiv) {
                messageDiv.innerHTML = this.message;
                messageDiv.style.color = this.color;
            }
        }
    }

    checkNick(): void {
        const nickInput: HTMLInputElement | null = document.getElementById("nick") as HTMLInputElement;
        if (nickInput) {
            const nick: string = nickInput.value;
            if (this.nicks.includes(nick)) {
                this.message = "Nick no disponible";
                this.color = "red";
            }else if (nick==""){
                this.message = "Introduzca un nick";
                this.color = "red";
            } else {
                this.message = "Nick disponible";
                this.color = "green";
            }
            const messageDiv: HTMLElement | null = document.getElementById("nickContent");
            if (messageDiv) {
                messageDiv.innerHTML = this.message;
                messageDiv.style.color = this.color;
            }
        }
    }

    checkForm(): string {
        if (!this.user.mail && !this.edit)
            return "Introduzca un email. ";

        if (!this.user.nick && !this.edit)
            return "Introduzca un nick. ";

        if ((!this.user.pass || this.user.pass === "") && !this.edit)
            return "Introduzca una contraseña. ";

        return "";
    }
}