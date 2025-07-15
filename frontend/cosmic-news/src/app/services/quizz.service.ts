import { Injectable } from "@angular/core";
import { Observable, throwError, catchError, map } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Quizz } from "../models/quizz.model";

const urlQuizzes = '/api/quizzes'

@Injectable({ providedIn: 'root' })
export class QuizzService {
    constructor(private httpClient: HttpClient) { }

    addOrUpdateQuizz(quizz: Quizz){
        if (!quizz.id){
            return this.addQuizz(quizz);
        } else{
            return this.updateQuizz(quizz);
        }
    }

    getQuizz(id: number): Observable<Object> {
        return this.httpClient.get(urlQuizzes + "/" + id);
    }

    getBadge(id: number) {
        return this.httpClient.get(urlQuizzes + "/" + id + "/badge", { responseType: 'arraybuffer' });
    }

    deleteQuizz(id: number | undefined) {
        return this.httpClient.delete(urlQuizzes + "/" + id);
    }

    editQuizz(id: number | undefined, quizz: Quizz) {
        this.httpClient.put(urlQuizzes + "/" + id, quizz).subscribe();
        return true
    }

    newQuizz(quizz: Quizz): Observable<Object>{
        return this.httpClient.post(urlQuizzes, quizz);
    }

    setBadge(quizz: Quizz, formData: FormData) {
        return this.httpClient.post(urlQuizzes + "/"+ quizz.id + '/badge', formData)
                .pipe(
                    catchError(error => this.handleError(error))
                );
    }

    private addQuizz(quizz: Quizz){
        return this.httpClient.post(urlQuizzes,quizz).pipe(
            catchError(error => this.handleError(error))
        );
    }

    private updateQuizz(quizz:Quizz){
        return this.httpClient.put(urlQuizzes+"/"+quizz.id,quizz).pipe(
            catchError(error => this.handleError(error))
        );
    }

    submitQuizz(id: number | undefined, quizz: Quizz) {
    return this.httpClient.post('/api/quizzes/' + id + '/submit', quizz)
    .pipe(catchError(error => this.handleError(error)));
    }

    private handleError(error: any) {
        console.error(error);
        return throwError("Server error (" + error.status + "):" + error.text())
    }
}