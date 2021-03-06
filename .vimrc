set nocompatible
filetype off

set rtp+=~/.vim/bundle/Vundle.vim
call vundle#begin()

"Bundle 'Shougo/vimproc'
"Bundle 'Shougo/unite.vim'
"Bundle 'm2mdas/phpcomplete-extended'

Plugin 'VundleVim/Vundle.vim'
Plugin 'Valloric/YouCompleteMe'
Plugin 'nerdtree'
Plugin 'ctrlp.vim'
Plugin 'auto-pairs'
Plugin 'scwood/vim-hybrid'
Plugin 'vim-twig'
Plugin 'dbext.vim'
Plugin 'alessandroyorba/alduin'
Plugin 'pbrisbin/vim-colors-off'
Plugin 'mileszs/ack.vim'
Plugin 'rename.vim'
Plugin 'ervandew/supertab'
Plugin 'SirVer/ultisnips'
Plugin 'honza/vim-snippets'
Plugin 'pangloss/vim-javascript'
Plugin 'javascript-libraries-syntax'
Plugin 'Syntastic'
Plugin 'surround.vim'
Plugin 'camelcasemotion'
Plugin 'kristijanhusak/vim-hybrid-material'
Plugin 'vim-airline/vim-airline'
Plugin 'vim-airline/vim-airline-themes'

call vundle#end()
filetype pluginindent on

" Plugins

" Airline
" let g:airline#extensions#tabline#enabled = 1
let g:airline_theme='hybrid'
set laststatus=2

" NERDTree
":nnoremap <leader>d :NERDTreeToggle<cr>
:nnoremap <D-1> :NERDTreeToggle<cr>

" CtrlP
let g:ctrlp_map = '<c-p>'
let g:ctrlp_cmd = 'CtrlP'
set wildignore+=*/bin/**,node_modules
let g:ctrlp_use_caching=0

" YCM
" Shorter RefactorRename
:nnoremap <F6> :YcmCompleter RefactorRename<space>

" make YCM compatible with UltiSnips (using supertab)
let g:ycm_key_list_select_completion = ['<C-n>', '<Down>']
let g:ycm_key_list_previous_completion = ['<C-p>', '<Up>']
let g:SuperTabDefaultCompletionType = '<C-n>'

" Eclim
" make YCM compatible with eclim
let g:EclimCompletionMethod = 'omnifunc'

" Ack
" Mapping :Ack command
:nnoremap <leader>f :Ack<space>
" Remapping substitution command. Not really Ack, but related to its use.
:nnoremap <leader>r :cdo s///gc<left><left><left><left>

" better key bindings for for UltiSnipsExpandTrigger
let g:UltiSnipsExpandTrigger = "<tab>"
let g:UltiSnipsJumpForwardTrigger = "<tab>"
let g:UltiSnipsJumpBackwardTrigger = "<s-tab>"

" Setting up used libraries for javascript-libraries-syntax
let g:used_javascript_libs = 'jquery,angularjs,angularui,requirejs'

" Syntastic
set statusline+=%#warningmsg#
set statusline+=%{SyntasticStatuslineFlag()}
set statusline+=%*

let g:syntastic_always_populate_loc_list = 1
let g:syntastic_auto_loc_list = 1
let g:syntastic_check_on_open = 1
let g:syntastic_check_on_wq = 1

let g:syntastic_mode_map = {"mode": "active", "passive_filetypes": ["tex"]}

" End plugins

" Setting target directories for backup, swap and undo
set backupdir=~/.vim/backup//
set directory=~/.vim/swap//
set undodir=~/.vim/undo//

set enc=utf-8

set number
set nowrap
syntax on

set foldmethod=syntax
"set foldcolumn=4

let php_folding=1

augroup no_initial_fold
    autocmd!
    autocmd BufWinEnter * let &foldlevel = max(map(range(1, line('$')), 'foldlevel(v:val)'))
augroup END

" Setting up command for running shell scripts
" and displaying output in scratch buffer
:command! -nargs=* -complete=shellcmd R new | res 10 | setlocal buftype=nofile bufhidden=hide noswapfile | r !<args>

" Color schemes
set background=dark
set t_Co=256
colorscheme hybrid_material
"colorscheme alduin
"colorscheme hybrid
""colorscheme off

if has('gui_running')
    set guifont=Inconsolata-dz:h12
    " Remove ugly scrollbar
    set guioptions-=r
endif

" Setting tabs
:set softtabstop=4 shiftwidth=4 expandtab

" Remappings
:let mapleader = "-"

"Opening and sourcing .vimrc
:nnoremap <leader>ev :vsplit $MYVIMRC<cr>
:nnoremap <leader>sv :source $MYVIMRC<cr>

" Writing å
:inoremap aa <c-k>aa

:nnoremap <c-l> gt
:nnoremap <c-h> gT

" Operate on text between quotes
:onoremap q' i'
:onoremap q" i"

" Operate on text between parentheses
:onoremap p i(
" Delete until closing parenthesis:
:onoremap cp %

"Saving
:nnoremap <c-s> :w<cr>
" Exiting insert mode and saving at the same time
:inoremap <leader>w <esc>:w<cr>
:nnoremap <leader>w :w<cr>

" comment out line
:nnoremap <s-c> 0wi//<esc>
" move left in insert mode to clear auto-paired character
:inoremap <leader>l <esc>la

" Moving to end of line easily
:nnoremap <leader>ll $

" Open snippet file
:nnoremap <leader>snip  :tabe /Users/simpa2k/.vim/bundle/vim-snippets/snippets/

" Move line up or down
:nnoremap <D-k> dd2kp
:nnoremap <D-j> ddp

"File type specific remappings

" Html
augroup filetype_html
    autocmd!
    autocmd FileType html set omnifunc=htmlcomplete#CompleteTags
    autocmd FileType html inoremap <lt>/ </<c-x><c-o>
augroup END

" Python
augroup filetype_python
    autocmd!
    :autocmd FileType python nnoremap <s-c> 0i#<esc>j
augroup END

" PHP
augroup filetype_php
    autocmd!
    " omnifunc completion
    autocmd FileType php set omnifunc=phpcomplete#CompletePHP
augroup END

" Java
augroup filetype_java
    autocmd!
    " compile and run, assumes that a source and a bin folder is used and that
    " vim is run from the source folder.
    autocmd FileType java nnoremap <leader>run :R mkdir -p ../bin/ && javac -d ../bin/ main/Main.java && cd ../bin/ && java main/Main<cr>
    " eclim
    autocmd FileType java nnoremap <silent> <buffer> <leader>i :JavaImport<cr>
    autocmd FileType java nnoremap <leader>run :Java<cr>
    autocmd FileType java nnoremap <leader>jn :JavaNew class<space>
augroup END

" LaTex
" Tell vim to prefer LaTex where there is not enough information
let g:tex_flavor = "latex"

augroup filetype_tex
    autocmd!
    autocmd FileType tex set tw=150
    autocmd FileType tex nnoremap <leader>fp vipgq
augroup END
