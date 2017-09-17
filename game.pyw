import tkinter as tk
import random
import time

root = tk.Tk()
root.resizable(False,False)

back = '#eeeebb'
blue_castle = tk.PhotoImage(file='images/blue_castle.gif')
red_castle = tk.PhotoImage(file='images/red_castle.gif')
green_castle = tk.PhotoImage(file='images/green_castle.gif')
surrendered_castle = tk.PhotoImage(file='images/surrendered_castle.gif')
spruces = tk.PhotoImage(file='images/spruces.gif')
oaks = tk.PhotoImage(file='images/oaks.gif')
gravel = tk.PhotoImage(file='images/gravel.gif')
overlays = {'blue castle':blue_castle,'red castle':red_castle,'green castle':green_castle,
            'surrendered castle':surrendered_castle,'spruces':spruces,'oaks':oaks,
            'none':None}

def callback(event):
    event.widget.bordify()

class SquareLand(tk.Canvas):
    def __init__(self, master=None):
        tk.Canvas.__init__(self, master, width=32, height=32, highlightthickness=0)
        self.bind("<Button-1>", callback)
        self.focused = False
        
        seed = random.random()
        if seed < .005:
            self.overlay = 'blue castle'
        elif seed < .01:
            self.overlay = 'red castle'
        elif seed < .015:
            self.overlay = 'green castle'
        elif seed < .16:
            self.overlay = 'spruces'
        elif seed < .3:
            self.overlay = 'oaks'
        else:
            self.overlay = 'none'
        
        self.redraw()

    def redraw(self):
        self.create_rectangle(0,0,32,32,fill='#aaddbb',outline='')
        if self.focused:
            self.create_rectangle(2,2,30,30,fill='',outline='red')
        self.create_image(0,0,anchor='nw',image=gravel)
        self.create_image(0,0,anchor='nw',image=overlays[self.overlay])

    def bordify(self):
        self.delete('all')
        paintboard = self.master
        
        paintboard.focused.focused = False
        paintboard.focused.redraw()
        
        paintboard.focused = self
        self.focused = True
        self.redraw()

        paintboard.master.d['text'] = self.overlay

class Application(tk.Frame):
    def __init__(self, master=None):
        tk.Frame.__init__(self, master,bg=back,cursor='target')
        self.pack()
        self.create_widgets()
        
    def create_widgets(self):     
        self.paintboard = tk.Frame(self, width=800, height=480)
        self.paintboard.pack(side='top')

        self.paintboard.squares = []
        for i in range(15):
            row = []
            for j in range(25):
                sl = SquareLand(self.paintboard)
                sl.grid(row = i, column = j)
                row += [sl]
            self.paintboard.squares += [row]
        self.paintboard.focused = self.paintboard.squares[0][0]

        self.textframe = tk.Frame(self, width=800, height=160,pady=40, bg=back)
        self.textframe.pack(side = 'bottom')
        
        self.a = tk.Label(self.textframe, text='A is for Apple',
                          font=('Georgia',12), bg=back)
        self.a.grid(row=0,column=0)
        self.b = tk.Label(self.textframe, text='B is for Banana',
                          font=('Georgia',12), bg=back)
        self.b.grid(row=0,column=1)
        self.c = tk.Label(self.textframe, text='C is for Coconut',
                          font=('Georgia',12), bg=back)
        self.c.grid(row=1,column=0)
        self.d = tk.Label(self.textframe, text='D is for Durian',
                          font=('Georgia',12), bg=back)
        self.d.grid(row=1,column=1)

app = Application(master=root)
app.master.title('This is a game.')
app.mainloop()
